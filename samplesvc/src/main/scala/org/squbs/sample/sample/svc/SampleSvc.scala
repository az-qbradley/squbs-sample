/*
 *  Copyright 2015 PayPal
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.squbs.sample.sample.svc

import akka.actor.{ActorLogging, Actor, Props}
import spray.can.Http
import spray.routing._
import Directives._
import spray.http._
import MediaTypes._
import spray.httpx.encoding.Gzip

import org.squbs.unicomplex.RouteDefinition
import org.squbs.unicomplex.MediaTypeExt._

import org.squbs.sample.sample.msgs._
import spray.httpx.Json4sJacksonSupport
import org.json4s.{DefaultFormats, NoTypeHints, Formats}

// this class defines our service behavior independently from the service actor
class SampleSvc extends RouteDefinition {

  def route =
    path("hello") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Say hello to <i>sample</i> on <i>squbs</i>, <i>spray-routing</i> and <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    } ~
    path("sample.json") {
      object JsonSupport extends Json4sJacksonSupport {
        implicit def json4sJacksonFormats: Formats = DefaultFormats.withHints(NoTypeHints)
      }
      import JsonSupport._
      get {
        parameter("msg" ? "Message is empty") {
          msg => {
            encodeResponse(Gzip) {
              respondWithMediaType(`application/json`) {
                complete {
                  Event(msg) // return object directly, marshaller in JsonSupport will serialize it according to the content-type
                }
              }
            }
          }
        }
      } ~
      post {
        entity(as[Event]) { body => // Unmarshaller in JsonSupport will deserialize it if the content-type is application/json
          respondWithMediaType(`text/html`) {
            complete {
              s"${body.getClass}:$body"
            }
          }
        }
      }
    } ~  
    path(""".*\.html""".r) { name =>
      encodeResponse(Gzip) {
        getFromResource("html/" + name)
      }
    } ~  
    path("events") {
      get { ctx =>
        context.actorOf(Props(classOf[Mediator] ,ctx))
      }
    }
  }

class Mediator(ctx: RequestContext) extends Actor with ActorLogging {

  context.actorSelection("/user/samplecube/sample") ! StartEvents
  val responseStart = HttpResponse(entity = HttpEntity(`text/event-stream`, toSSE("Starting")))
  ctx.responder ! ChunkedResponseStart(responseStart)

  def toSSE(msg: String) = "event: lyric\ndata: " + msg.replace("\n", "\ndata: ") + "\n\n"
  val streamEnd = "event: streamEnd\ndata: End of stream\n\n"

  def receive = {
    case Event(msg) => 
      val eventMessage = toSSE(msg)
      log.info('\n' + eventMessage)
      ctx.responder ! MessageChunk(eventMessage)

    case EndEvents  =>
      log.info('\n' + streamEnd)
      ctx.responder ! MessageChunk(streamEnd)
      ctx.responder ! ChunkedMessageEnd()
      context.stop(self)

    // Connection closed sent from ctx.responder
    case ev: Http.ConnectionClosed =>
      log.warning("Connection closed, {}", ev)
      context.stop(self)        
  }
}

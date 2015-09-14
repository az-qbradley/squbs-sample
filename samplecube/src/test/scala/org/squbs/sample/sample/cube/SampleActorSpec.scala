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
package org.squbs.sample.sample.cube

import scala.concurrent.duration._
import akka.actor.Props
import org.scalatest._
import org.squbs.testkit.SimpleTestKit
import org.squbs.sample.sample.msgs._

class SampleActorSpec extends SimpleTestKit with FunSpecLike {

  describe("The SampleActor") {

    it ("Should respond with Events after receiving StartEvents") {
      system.actorSelection("/user/samplecube/sample") ! StartEvents
      expectMsgType[Event](10 seconds)
    }

    it ("Should be creatable using normal actor creation") {
      system.actorOf(Props[SampleActor]) ! StartEvents
      expectMsgType[Event](10 seconds)
    }
  }
}

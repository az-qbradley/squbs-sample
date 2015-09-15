import Versions._

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-slf4j" % akkaV,
  "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "io.spray" %% "spray-routing-shapeless2" % sprayV,
  "io.spray" %% "spray-http" % sprayV,
  "io.spray" %% "spray-httpx" % sprayV,
  "io.spray" %% "spray-can" % sprayV,
  "io.spray" %% "spray-testkit" % sprayV % "test",
  "org.json4s" %% "json4s-native" % "3.2.9" excludeAll(
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule(organization = "org.scala-lang.modules")
    ),
  "org.json4s" %% "json4s-jackson" % "3.2.9" excludeAll(
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule(organization = "org.scala-lang.modules")
    ),
  "org.squbs" %% "squbs-unicomplex" % squbsV,
  "org.squbs" %% "squbs-actormonitor" % squbsV,
  "org.scalatest" %% "scalatest" % scalatestV % "test",
  "org.squbs" %% "squbs-testkit" % squbsV % "test"
)

mainClass in (Compile, run) := Some("org.squbs.unicomplex.Bootstrap")

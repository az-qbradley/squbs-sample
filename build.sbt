resolvers in ThisBuild += Resolver.sonatypeRepo("snapshots") 

scalaVersion in ThisBuild := "2.11.7"

name := "sample"

organization in ThisBuild := "org.squbs.sample"

version in ThisBuild := "0.0.1-SNAPSHOT"

publishArtifact := false

checksums in ThisBuild := Nil

crossPaths in ThisBuild := false

fork in ThisBuild := true

lazy val `samplemsgs` = project

lazy val `samplecube` = project dependsOn `samplemsgs`

lazy val `samplesvc` = project dependsOn (`samplemsgs`, `samplecube`)

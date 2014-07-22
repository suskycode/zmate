name := "zmate"

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.4",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4",
  "org.seleniumhq.selenium" % "selenium-java" % "2.42.2"
)

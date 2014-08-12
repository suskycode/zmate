name := "zmate"

version := "1.0"

scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.4",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4",
  "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.1.0",
  "org.seleniumhq.selenium" % "selenium-java" % "2.42.2"
//  "org.quartz-scheduler" % "quartz" % "2.2.1",
// "org.quartz-scheduler" % "quartz-jobs" % "2.2.1"
)

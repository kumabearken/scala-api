name := """api"""
organization := "scala-project"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies ++= Seq(
"mysql" % "mysql-connector-java" % "8.0.33",
"com.typesafe.play" %% "play-slick" % "4.0.2",
"com.typesafe.play" %% "twirl-api" % "1.6.7",
"com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"
)
// Adds additional packages into Twirl
TwirlKeys.templateImports += "scala-project.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "scala-project.binders._"
enablePlugins(JavaAppPackaging)
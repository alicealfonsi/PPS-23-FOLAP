ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

// For Scalafix
ThisBuild / semanticdbEnabled := true
scalacOptions ++= Seq("-Wunused:all")

lazy val root = (project in file("."))
  .settings(
    name := "FOLAP",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test,
  )

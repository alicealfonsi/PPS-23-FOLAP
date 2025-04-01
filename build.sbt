ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

// For Scalafix
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild /

lazy val root = (project in file("."))
  .settings(
    name := "FOLAP"
      scalacOptions ++= Seq("-Wunused:imports")
  )


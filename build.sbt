val scala3Version = "3.7.0"

scalacOptions ++= Seq("-Wunused:all", "-explain")

lazy val core = project
  .in(file("."))
  .settings(
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    // For Scalafix
    semanticdbEnabled := true,
    name := "FOLAP",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"
  )

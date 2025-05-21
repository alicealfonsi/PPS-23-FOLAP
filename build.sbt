val scala3Version = "3.3.6"

ThisBuild / scalacOptions ++= Seq("-Wunused:all", "-explain")

lazy val core = project
  .in(file("./core"))
  .settings(
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    // For Scalafix
    semanticdbEnabled := true,
    name := "FOLAP",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"
  )

lazy val typingDSL = project
  .in(file("./typingDSL"))
  .settings(
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    // For Scalafix
    semanticdbEnabled := true,
    name := "FOLAP (typing DSL)",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"
  )

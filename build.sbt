val scala3Version = "3.3.6"

ThisBuild / scalacOptions ++= Seq("-Wunused:all", "-explain")

lazy val core = project
  .in(file("./core"))
  .settings(
    version := "1.0.1",
    scalaVersion := scala3Version,
    // For Scalafix
    semanticdbEnabled := true,
    name := "FOLAP",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test",
    assembly / assemblyOutputPath := file("./FOLAP.jar")
  )

lazy val modelDefinition = project
  .in(file("./modelDefinition"))
  .settings(
    version := "1.0.1",
    scalaVersion := scala3Version,
    // For Scalafix
    semanticdbEnabled := true,
    name := "FOLAP (typing DSL)",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test",
    assembly / assemblyOutputPath := file("./FOLAP-modelDSL.jar")
  )

lazy val examples = project
  .in(file("./examples"))
  .dependsOn(core)
  .dependsOn(modelDefinition)
  .settings(
    version := "1.0.1",
    scalaVersion := scala3Version,
    // For Scalafix
    semanticdbEnabled := true,
    name := "FOLAP (examples)",
    assembly / mainClass := Some("mycompany.dw.Main"),
    assembly / assemblyOutputPath := file("./FOLAP-example.jar")
  )

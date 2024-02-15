ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

val fs2Version = "3.9.4"

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    Compile / run / fork := true,
    name := "Streaming",
    mainClass := Some("com.edufuga.scala.streaming.Main")
  )
  .aggregate(core, data)
  .dependsOn(core, data)

lazy val data = (project in file("data"))
  .settings(
    Compile / run / fork := true,
    name := "Core",
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % fs2Version,
      "co.fs2" %% "fs2-io" % fs2Version,
      "org.scala-lang.modules" %% "scala-xml" % "2.1.0"
    )
  )
  .aggregate(core)
  .dependsOn(core)

lazy val core = (project in file("core"))
  .settings(
    Compile / run / fork := true,
    name := "Core"
  )

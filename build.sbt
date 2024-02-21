ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

val fs2Version = "3.9.4"

val commonSettings = Seq(
  Compile / run / fork := true,
)

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings)
  .settings(
    name := "Streaming",
    mainClass := Some("com.edufuga.scala.streaming.Streamer"),
    maintainer := " efugarolas@brox.de"
  )
  .aggregate(core, data)
  .dependsOn(core, data)

lazy val data = (project in file("data"))
  .settings(commonSettings)
  .settings(
    name := "Core",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-xml" % "2.1.0",
      "co.fs2" %% "fs2-core" % fs2Version,
      "co.fs2" %% "fs2-io" % fs2Version
    )
  )
  .aggregate(core)
  .dependsOn(core)

lazy val core = (project in file("core"))
  .settings(commonSettings)
  .settings(
    name := "Core"
  )

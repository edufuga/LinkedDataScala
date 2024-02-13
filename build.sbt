ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

val fs2Version = "3.9.4"

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    Compile / run / fork := true,
    name := "Streaming",
    idePackagePrefix := Some("com.edufuga.scala.streaming"),
    mainClass := Some("com.edufuga.scala.streaming.Main"),
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % fs2Version,
      "co.fs2" %% "fs2-io" % fs2Version,
      "org.scala-lang.modules" %% "scala-xml" % "2.1.0"
    )
  )

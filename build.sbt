ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

val fs2Version = "3.9.4"

lazy val root = (project in file("."))
  .settings(
    name := "Streaming",
    idePackagePrefix := Some("com.edufuga.scala.streaming"),
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % fs2Version,
      "co.fs2" %% "fs2-io" % fs2Version,
    )
  )

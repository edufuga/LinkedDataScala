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
    name := "Streamer",
    mainClass := Some("com.edufuga.scala.streaming.Streamer"),
    maintainer := " efugarolas@brox.de"
  )
  .aggregate(entities, data)
  .dependsOn(entities, data)

lazy val data = (project in file("data"))
  .settings(commonSettings)
  .settings(
    name := "Data",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-xml" % "2.1.0",
      "co.fs2" %% "fs2-core" % fs2Version,
      "co.fs2" %% "fs2-io" % fs2Version
    )
  )
  .aggregate(entities, operations, entity_parsers, entity_operations)
  .dependsOn(entities, operations, entity_parsers, entity_operations)

lazy val entities = (project in file("entities"))
  .settings(commonSettings)
  .settings(
    name := "Entities"
  )

lazy val operations = (project in file("operations"))
  .settings(commonSettings)
  .settings(
    name := "Operations"
  )

lazy val entity_operations = (project in file("entity_operations"))
  .settings(commonSettings)
  .settings(
    name := "Entity Operations"
  )
  .aggregate(entities, operations)
  .dependsOn(entities, operations)

lazy val entity_parsers = (project in file("entity_parsers"))
  .settings(commonSettings)
  .settings(
    name := "Entity Parsers",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-xml" % "2.1.0"
    )
  )
  .aggregate(entities)
  .dependsOn(entities)

package com.edufuga.scala.streaming

import cats.effect.{IO, IOApp}
import fs2.io.file.{Files, Path}
import fs2.{Pipe, Stream, text}

import java.net.URL

object Main extends IOApp.Simple {
  private def pathOf(file: String): Path = {
    val readFromJavaResource: URL = getClass.getClassLoader.getResource(file)
    val readFromJavaPath: java.nio.file.Path = java.nio.file.Paths.get(readFromJavaResource.toURI).toAbsolutePath
    Path.fromNioPath(readFromJavaPath)
  }

  val source: Stream[IO, Byte] = Files[IO].readAll(pathOf("products.csv"))

  def lineParser[F[_]]: Pipe[F, Byte, String] =
    _.through(text.utf8.decode)
      .through(text.lines)
      .drop(1)
      //.map(_.split(',').toList) // Too simplistic. It doesn't work with our "ugly data".

  val sourceThroughParser: Stream[IO, String] = source.through(lineParser)

  val processedSourceStream: Stream[IO, String] = sourceThroughParser.evalTap(IO.println)

  // TODO: Create Scala objects for Product and Service (lists of entities).

  override def run: IO[Unit] = processedSourceStream.compile.drain
}
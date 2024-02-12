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

  def productsParser[F[_]]: Pipe[F, Byte, Product] =
    _.through(text.utf8.decode)
      .through(text.lines)
      .drop(1)
      .map(Parsers.product)

  val sourceThroughProductParser: Stream[IO, Product] = source.through(productsParser)

  val processedProductsStream: Stream[IO, Product] = sourceThroughProductParser.evalTap(IO.println)

  override def run: IO[Unit] = processedProductsStream.compile.drain
}
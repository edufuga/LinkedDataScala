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

  def entitiesParser[F[_], E](toMaybeEntity: String => Option[E]): Pipe[F, Byte, Option[E]] =
    _.through(text.utf8.decode)
      .through(text.lines)
      .drop(1)
      .map(toMaybeEntity)

  val productsStream: Stream[IO, Option[Product]] =
    Files[IO].readAll(pathOf("products.csv"))
    .through(entitiesParser(Parsers.product))
    .evalTap(IO.println)

  val servicesStream: Stream[IO, Option[Service]] =
    Files[IO].readAll(pathOf("services.csv"))
    .through(entitiesParser(Parsers.service))
    .evalTap(IO.println)

  override def run: IO[Unit] = for {
    _ <- IO.println("Processing stream of products")
    _ <- productsStream.compile.drain
    _ <- IO.println("Processing stream of services")
    _ <- servicesStream.compile.drain
  } yield ()
}
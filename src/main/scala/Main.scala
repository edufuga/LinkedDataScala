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

  val productsSource: Stream[IO, Byte] = Files[IO].readAll(pathOf("products.csv"))
  def productsParser[F[_]]: Pipe[F, Byte, Option[Product]] = entitiesParser(Parsers.product)
  val productsSourceThroughParser: Stream[IO, Option[Product]] = productsSource.through(productsParser)
  val productsProcessedStream: Stream[IO, Option[Product]] = productsSourceThroughParser.evalTap(IO.println)

  val servicesSource: Stream[IO, Byte] = Files[IO].readAll(pathOf("services.csv"))
  def servicesParser[F[_]]: Pipe[F, Byte, Option[Service]] = entitiesParser(Parsers.service)
  val servicesSourceThroughParser: Stream[IO, Option[Service]] = servicesSource.through(servicesParser)
  val servicesprocessedStream: Stream[IO, Option[Service]] = servicesSourceThroughParser.evalTap(IO.println)

  override def run: IO[Unit] = for {
    _ <- IO.println("Processing stream of products")
    _ <- productsProcessedStream.compile.drain
    _ <- IO.println("Processing stream of services")
    _ <- servicesprocessedStream.compile.drain
  } yield ()
}
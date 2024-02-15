package com.edufuga.scala.streaming

import com.edufuga.scala.core.{Organisation, Service}
import com.edufuga.scala.data.{CSVParsers, XMLParsers}

import cats.effect.{ExitCode, IO, IOApp}
import fs2.io.file.{Files, Path}
import fs2.{Pipe, Stream, text}

import java.net.URL
import scala.xml.XML.loadFile

object Main extends IOApp {
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

  def productsStream(file: String): Stream[IO, Option[Product]] =
    Files[IO].readAll(pathOf(file))
    .through(entitiesParser(CSVParsers.product))
    .evalTap(IO.println)

  def servicesStream(file: String): Stream[IO, Option[Service]] =
    Files[IO].readAll(pathOf(file))
    .through(entitiesParser(CSVParsers.service))
    .evalTap(IO.println)

  def organisation(file: String): Option[Organisation] =
    XMLParsers.organisation(loadFile(pathOf(file).toString))

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO.println("Processing 'products.csv' and 'services.csv'")
      _ <- IO.println("Processing stream of products")
      _ <- productsStream("products.csv").compile.drain
      _ <- IO.println("Processing stream of services")
      _ <- servicesStream("services.csv").compile.drain
      _ <- IO.println("Processing the organisation file 'orgmap.xml'")
      _ <- IO.println { organisation("orgmap.xml") }
    } yield ExitCode.Success
  }
}
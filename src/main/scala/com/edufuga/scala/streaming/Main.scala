package com.edufuga.scala.streaming

import com.edufuga.scala.core.{Organisation, Service}
import cats.effect.{ExitCode, IO, IOApp}
import com.edufuga.scala.data.access.streamed.file.{FileStreamedProductsDAO, FileStreamedServicesDAO}
import com.edufuga.scala.data.parsers.xml.XMLParsers
import fs2.io.file.Path
import fs2.Stream

import scala.xml.XML.loadFile

object Main extends IOApp {
  private def pathOf(file: String): Path = {
    val readFromJavaPath: java.nio.file.Path = java.nio.file.Paths.get(file).toAbsolutePath
    Path.fromNioPath(readFromJavaPath)
  }

  def productsStream(file: String): Stream[IO, Option[Product]] = FileStreamedProductsDAO(file).readAll

  def servicesStream(file: String): Stream[IO, Option[Service]] = FileStreamedServicesDAO(file).readAll

  def organisation(file: String): Option[Organisation] =
    XMLParsers.organisation(loadFile(pathOf(file).toString))

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO.println("Processing 'products.csv' and 'services.csv'")
      products = args(0)
      services = args(1)
      orga = args(2)
      _ <- IO.println(s"Processing stream of products '$products'.")
      _ <- productsStream(products).compile.drain
      _ <- IO.println(s"Processing stream of services '$services'.")
      _ <- servicesStream(services).compile.drain
      _ <- IO.println("Processing the organisation file 'orgmap.xml'")
      _ <- IO.println { organisation(orga) }
    } yield ExitCode.Success
  }
}
package com.edufuga.scala.streaming

import com.edufuga.scala.core.Organisation
import cats.effect.{ExitCode, IO, IOApp}
import com.edufuga.scala.data.access.materialized.file.FileOrganisationDAO
import com.edufuga.scala.data.access.streamed.file.{FileStreamedProductsDAO, FileStreamedServicesDAO}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO.println("Processing 'products.csv' and 'services.csv'")
      products = args(0)
      services = args(1)
      orga = args(2)
      _ <- IO.println(s"Processing stream of products '$products'.")
      _ <- FileStreamedProductsDAO(products).readAll.compile.drain
      _ <- IO.println(s"Processing stream of services '$services'.")
      _ <- FileStreamedServicesDAO(services).readAll.compile.drain
      _ <- IO.println("Processing the organisation file 'orgmap.xml'")
      _ <- IO.println { FileOrganisationDAO(orga).readAll }
    } yield ExitCode.Success
  }
}

package com.edufuga.scala.streaming

import com.edufuga.scala.core.Organisation
import cats.effect.{ExitCode, IO, IOApp}
import com.edufuga.scala.data.access.materialized.file.FileMaterializingOrganisationDAO
import com.edufuga.scala.data.access.streamed.file.{FileStreamingProductsDAO, FileStreamingServicesDAO}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO.println("Processing 'products.csv', 'services.csv' and 'orgmap.xml'.")
      products = args(0)
      services = args(1)
      orga = args(2)
      _ <- IO.println(s"Processing stream of products '$products'.")
      _ <- FileStreamingProductsDAO(products).readAll.compile.drain
      _ <- IO.println(s"Processing stream of services '$services'.")
      _ <- FileStreamingServicesDAO(services).readAll.compile.drain
      _ <- IO.println("Processing the organisation file 'orgmap.xml'")
      _ <- IO.println { FileMaterializingOrganisationDAO(orga).readAll }
    } yield ExitCode.Success
  }
}

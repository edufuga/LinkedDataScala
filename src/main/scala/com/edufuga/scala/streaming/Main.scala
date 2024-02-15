package com.edufuga.scala.streaming

import com.edufuga.scala.core.Organisation
import cats.effect.{ExitCode, IO, IOApp}
import com.edufuga.scala.core.ProductTypes.ProductId
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
      _ <- FileStreamingProductsDAO(products).readAll.evalTap(IO.println).compile.drain

      _ <- IO.println(s"Processing stream of services '$services'.")
      _ <- FileStreamingServicesDAO(services).readAll.evalTap(IO.println).compile.drain

      _ <- IO.println("Processing the organisation file 'orgmap.xml'")
      _ <- IO.println { FileMaterializingOrganisationDAO(orga).readAll }

      _ <- IO.println(s"Finding a product by ID within the stream of products '$products'.")
      bingoProduct <- FileStreamingProductsDAO(products).readById(ProductId("X716-6172862")).compile.last
      _ <- IO.println("Bingo product: " + bingoProduct.flatten)

    } yield ExitCode.Success
  }
}

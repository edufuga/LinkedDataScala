package com.edufuga.scala.streaming

import com.edufuga.scala.core.Organisation
import cats.effect.{ExitCode, IO, IOApp}
import com.edufuga.scala.core.ProductTypes.ProductId
import com.edufuga.scala.core.ServiceTypes.ServiceId
import com.edufuga.scala.data.access.materialized.file.FileMaterializingOrganisationDAO
import com.edufuga.scala.data.access.streamed.file.{FileStreamingProductDAO, FileStreamingServiceDAO}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO.println("Processing 'products.csv', 'services.csv' and 'orgmap.xml'.")

      products = args(0)
      services = args(1)
      orga = args(2)

      _ <- IO.println(s"Processing stream of products '$products'.")
      _ <- FileStreamingProductDAO(products).readAll.evalTap(IO.println).compile.drain

      _ <- IO.println(s"Processing stream of services '$services'.")
      _ <- FileStreamingServiceDAO(services).readAll.evalTap(IO.println).compile.drain

      _ <- IO.println("Processing the organisation file 'orgmap.xml'")
      _ <- IO.println { FileMaterializingOrganisationDAO(orga).readAll }

      _ <- IO.println(s"Finding a product by ID within the stream of products '$products'.")
      bingoProduct <- FileStreamingProductDAO(products).readById(ProductId("X716-6172862")).compile.last
      _ <- IO.println("Bingo product: " + bingoProduct.flatten)

      _ <- IO.println(s"Finding a service by ID within the stream of services '$services'.")
      bingoService <- FileStreamingServiceDAO(services).readById(ServiceId("Y274-1029755")).compile.last
      _ <- IO.println("Bingo service: " + bingoService.flatten)

    } yield ExitCode.Success
  }
}

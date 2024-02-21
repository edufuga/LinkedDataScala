package com.edufuga.scala.streaming

import com.edufuga.scala.core.Organisation
import cats.effect.{ExitCode, IO, IOApp}
import com.edufuga.scala.core.ProductTypes.ProductId
import com.edufuga.scala.core.ServiceTypes.ServiceId
import com.edufuga.scala.data.access.materialized.file.FileMaterializingOrganisationDAO
import com.edufuga.scala.data.access.streamed.file.{ProductFileStreamingWithIODAO, ServiceFileStreamingWithIODAO}

import scala.util.{Failure, Success, Try}

class Streamer(
  productDAO: ProductFileStreamingWithIODAO,
  serviceDAO: ServiceFileStreamingWithIODAO,
  organisationDAO: FileMaterializingOrganisationDAO
) {
  def stream: IO[ExitCode] = {
    for {
      _ <- IO.println("Processing 'products.csv', 'services.csv' and 'orgmap.xml'.")

      _ <- IO.println(s"Processing stream of products.")
      _ <- productDAO.readAll.evalTap(IO.println).compile.drain

      _ <- IO.println(s"Processing stream of services.")
      _ <- serviceDAO.readAll.evalTap(IO.println).compile.drain

      _ <- IO.println("Processing the organisation file 'orgmap.xml'")
      _ <- IO.println { organisationDAO.readAll }

      _ <- IO.println(s"Finding a product by ID within the stream of products.")
      bingoProduct <- productDAO.readById(ProductId("X716-6172862")).compile.last
      _ <- IO.println("Bingo product: " + bingoProduct)

      _ <- IO.println(s"Finding a service by ID within the stream of services.")
      bingoService <- serviceDAO.readById(ServiceId("Y274-1029755")).compile.last
      _ <- IO.println("Bingo service: " + bingoService)

    } yield ExitCode.Success
  }
}

object Streamer extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    Try {
      val products: String = args(0)
      val services: String = args(1)
      val organisation: String = args(2)
      (products, services, organisation)
    } match
      case Failure(_) => IO {
        ExitCode.Error
      }
      case Success(p, s, o) =>
        val productsDAO = ProductFileStreamingWithIODAO(p)
        val servicesDAO = ServiceFileStreamingWithIODAO(s)
        val organisationDAO = FileMaterializingOrganisationDAO(o)

        val streamer: Streamer = new Streamer(productsDAO, servicesDAO, organisationDAO)
        streamer.stream
  }
}

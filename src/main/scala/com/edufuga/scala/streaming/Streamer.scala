package com.edufuga.scala.streaming

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*
import com.edufuga.scala.core.*
import com.edufuga.scala.core.ProductTypes.ProductId
import com.edufuga.scala.core.ServiceTypes.ServiceId
import com.edufuga.scala.data.access.effectful.FullOrganisationTypeLevelEffectfulCombinationDAO
import com.edufuga.scala.data.access.entities.{FullOrganisationTypeLevelEffectfulDAO, OrganisationMaterializedDAO, ProductTypeLevelEffectfulStreamingDAO, ServiceTypeLevelEffectfulStreamingDAO}
import com.edufuga.scala.data.access.materialized.file.FileMaterializingOrganisationDAO
import com.edufuga.scala.data.access.streamed.file.{ProductFileStreamingWithIODAO, ServiceFileStreamingWithIODAO}

import scala.util.{Failure, Success, Try}

class Streamer(
  productDAO: ProductTypeLevelEffectfulStreamingDAO,
  serviceDAO: ServiceTypeLevelEffectfulStreamingDAO,
  organisationDAO: OrganisationMaterializedDAO,
  fullOrganisationDAO: FullOrganisationTypeLevelEffectfulDAO
) {
  def stream: IO[ExitCode] = {
    for {
      _ <- IO.println("Processing 'products.csv', 'services.csv' and 'orgmap.xml'.")

      _ <- IO.println(s"Processing stream of products.")
      products <- productDAO.readAll.compile.toList
      _ <- IO.println(products)

      _ <- IO.println(s"Processing stream of services.")
      services <- serviceDAO.readAll.compile.toList
      _ <- IO.println(services)

      _ <- IO.println("Processing the organisation file 'orgmap.xml'")
      maybeOrganisation = organisationDAO.readAll
      _ <- IO.println(maybeOrganisation)

      _ <- IO.println(s"Finding a product by ID within the stream of products.")
      bingoProduct <- productDAO.readById(ProductId("X716-6172862"))
      _ <- IO.println("Bingo product: " + bingoProduct)

      _ <- IO.println(s"Finding a service by ID within the stream of services.")
      bingoService <- serviceDAO.readById(ServiceId("Y274-1029755"))
      _ <- IO.println("Bingo service: " + bingoService)

      _ <- IO.println(s"Finding several products by their IDs within the stream of products.")
      severalProducts <- productDAO.readByIds(List(ProductId("O184-6903943"), ProductId("N180-3300253"))).compile.last
      _ <- IO.println("Several products: " + severalProducts)

      _ <- IO.println("Processing the full organisation. This includes resolving the linked products and services.")
      organisation <- fullOrganisationDAO.readAll
      _ <- IO.println(organisation)
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
        val productsDAO: ProductTypeLevelEffectfulStreamingDAO = ProductFileStreamingWithIODAO(p)
        val servicesDAO: ServiceTypeLevelEffectfulStreamingDAO = ServiceFileStreamingWithIODAO(s)

        val productsFromIds: List[ProductId] => IO[List[Product]] =
          productIds => productsDAO.readByIds(productIds).compile.toList
        val servicesFromIds: List[ServiceId] => IO[List[Service]] =
          serviceIds => servicesDAO.readByIds(serviceIds).compile.toList

        val organisationDAO: OrganisationMaterializedDAO = FileMaterializingOrganisationDAO(o)
        val fullOrganisationDAO: FullOrganisationTypeLevelEffectfulDAO =
          new FullOrganisationTypeLevelEffectfulCombinationDAO(productsFromIds, servicesFromIds, organisationDAO)

        val streamer: Streamer = new Streamer(productsDAO, servicesDAO, organisationDAO, fullOrganisationDAO)
        streamer.stream
  }
}

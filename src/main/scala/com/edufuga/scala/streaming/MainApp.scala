package com.edufuga.scala.streaming

import cats.effect.{ExitCode, IO, IOApp}
import com.edufuga.scala.entities.*
import com.edufuga.scala.entities.ProductTypes.ProductId
import com.edufuga.scala.entities.ServiceTypes.ServiceId
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.*
import com.edufuga.scala.operations.entity.implementation.effectful.FullOrganisationTypeLevelEffectfulCombinationDAO
import com.edufuga.scala.operations.entity.implementation.materialized.file.FileMaterializingOrganisationDAO
import com.edufuga.scala.operations.entity.implementation.streamed.file.*

import scala.util.{Failure, Success, Try}

object MainApp extends IOApp {
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

        // Notice that this Streamer is still quite implementation (TypeLevel) specific.
        // Both the interface (parameters) and the implementation are full of IO and Streams and stuff.
        val streamer: Streamer = new Streamer(productsDAO, servicesDAO, organisationDAO, fullOrganisationDAO)
        streamer.stream
  }
}

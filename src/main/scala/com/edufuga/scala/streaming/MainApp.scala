package com.edufuga.scala.streaming

import cats.effect.{ExitCode, IO, IOApp}
import com.edufuga.scala.entities.*
import com.edufuga.scala.entities.ProductTypes.ProductId
import com.edufuga.scala.entities.ServiceTypes.ServiceId
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.*
import com.edufuga.scala.operations.entity.implementation.effectful.graph.FullOrganisationTypeLevelEffectfulGraphDAO
import com.edufuga.scala.operations.entity.implementation.effectful.{FullOrganisationTypeLevelEffectfulCombinationDAO, FullServiceTypeLevelEffectfulCombinationDAO}
import com.edufuga.scala.operations.entity.implementation.materialized.file.FileMaterializingOrganisationDAO
import com.edufuga.scala.operations.entity.implementation.streamed.file.*

import scala.util.{Failure, Success, Try}

object MainApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    Try {
      val products: String = args(0)
      val services: String = args(1)
      val organisation: String = args(2)
      val organisationFile: String = args(3)

      (products, services, organisation, organisationFile)
    } match
      case Failure(_) => IO {
        ExitCode.Error
      }
      case Success(p, s, o, of) =>
        val productsDAO: ProductTypeLevelEffectfulStreamingDAO = ProductFileStreamingWithIODAO(p)
        val servicesDAO: ServiceTypeLevelEffectfulStreamingDAO = ServiceFileStreamingWithIODAO(s)

        val productsFromIds: List[ProductId] => IO[List[Product]] =
          productIds => productsDAO.readByIds(productIds).compile.toList
        val servicesFromIds: List[ServiceId] => IO[List[Service]] =
          serviceIds => servicesDAO.readByIds(serviceIds).compile.toList

        val organisationDAO: OrganisationMaterializedDAO = FileMaterializingOrganisationDAO(o)

        for {
          organisationGraph <- {
            val fullOrganisationDAO: FullOrganisationTypeLevelEffectfulDAO =
              new FullOrganisationTypeLevelEffectfulCombinationDAO(productsFromIds, servicesFromIds, organisationDAO)

            // Notice that this Streamer is still quite implementation (TypeLevel) specific.
            // Both the interface (parameters) and the implementation are full of IO and Streams and stuff.
            val streamer: Streamer = new Streamer(productsDAO, servicesDAO, organisationDAO, fullOrganisationDAO, of)
            streamer.stream
          }
          _ <- {
            // This DAO retrieves the "full services", using the "product IDs" contained in the 'shallow' services.
            val fullServicesDAO: FullServiceTypeLevelEffectfulStreamingDAO =
              FullServiceTypeLevelEffectfulCombinationDAO(
                productsFromIds,
                () => servicesDAO.readAll.compile.toList
              )

            // This DAO presents the "full organisation", using the organisation graph. It's just a wrapper of the API.
            val organisationGraphBasedDAO: FullOrganisationTypeLevelEffectfulDAO =
              FullOrganisationTypeLevelEffectfulGraphDAO(graph = () => organisationGraph)

            // Formulate and answer the business questions:
            // Notice that the passed 'organisationGraphBasedDAO' is graph-based. The business questions are thus
            // answered in terms of the _organisation graph_. Notice as well that this is just an implementation detail.
            val businessQuestions: BusinessQuestions =
              new BusinessQuestions(productsDAO, fullServicesDAO, organisationGraphBasedDAO)

            businessQuestions.stream
          }
        } yield ExitCode.Success
  }
}

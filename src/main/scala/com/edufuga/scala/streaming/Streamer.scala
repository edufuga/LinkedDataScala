package com.edufuga.scala.streaming

import cats.effect.{ExitCode, IO}
import com.edufuga.scala.entities.ProductTypes.ProductId
import com.edufuga.scala.entities.ServiceTypes.ServiceId
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.{FullOrganisationTypeLevelEffectfulDAO, OrganisationMaterializedDAO, ProductTypeLevelEffectfulStreamingDAO, ServiceTypeLevelEffectfulStreamingDAO}

// Notice that this Streamer is still quite implementation (TypeLevel) specific.
// Both the interface (parameters) and the implementation are full of IO and Streams and stuff.
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
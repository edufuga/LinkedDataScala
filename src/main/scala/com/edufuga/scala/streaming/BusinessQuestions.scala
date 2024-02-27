package com.edufuga.scala.streaming

import cats.implicits.* // .sequence
import cats.effect.{ExitCode, IO}
import com.edufuga.scala.entities.Service
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.*

class BusinessQuestions(
  productDAO: ProductTypeLevelEffectfulStreamingDAO,
  serviceDAO: ServiceTypeLevelEffectfulStreamingDAO,
  fullOrganisationDAO: FullOrganisationTypeLevelEffectfulDAO
) {
  def stream: IO[ExitCode] = {
    for {
      _ <- IO.println("Processing the full organisation. This includes resolving the linked products and services.")
      organisation <- fullOrganisationDAO.readAll
      _ <- IO.println(organisation)

      // Business question: Find services without a product manager
      // WARNING: The "Service" entity has a MANDATORY ProductManager.
      // As of now, this question makes no sense. It's not a valid question. The type system excludes it!

      // Business question: Find products without a product manager
      // WARNING: The "Product" entity has a MANDATORY ProductManager.
      // As of now, this question makes no sense. It's not a valid question. The type system excludes it!

      // FIXME/TODO: Perhaps we should allow at least the Products to contain potentially invalid (optional) Managers?
      // On the other hand, we could add this information in the 'error channel' of the IO, or using an "Either".
      // As of right now, using Option as the wrapper type is not enough to contain information about errors (which
      // include invalid data; i.e. products and services without a ProductManager). It's a design issue/decision.

      // Business question: Find services / products without responsible department
      // 1. Obtain all services / products, irrespective of the organisation mapping
      // 2. Obtain the services / products within the organisation mapping.
      // 3. Return the difference (i.e. the services / products NOT found in the list of all entities).
      _ <- IO.println(s"Obtain all services, irrespective of the organisation mapping")
      services <- serviceDAO.readAll.compile.toList
      _ <- IO.println(services)

      _ <- IO.println(s"Obtain the services within the organisation mapping.")
      // flatten: Option[List[List[Service]]] -> Option[List[Service]]
      // sequence: Option[List[Service]] -> List[Option[Service]]
      // filter nonEmpty + (safe) get: List[Option[Service]] -> List[Service]
      organisationServices = organisation.map { orga =>
        val unflattenedServices: List[List[Service]] = orga.departments.map(_.services)
        val flattenedServices: List[Service] = unflattenedServices.flatten
        flattenedServices
      }.sequence[List, Service]
        .filter(_.nonEmpty)
        .map(_.get)
      _ <- IO.println(organisationServices)

      _ <- IO.println(s"Return the difference (i.e. the services NOT provided by the organisation in any department).")
      serviceNotOfferedByTheOrganisation = services.diff(organisationServices)
      _ <- IO.println(serviceNotOfferedByTheOrganisation.map(s => (s.serviceName, s.id)))
      // Answer: "(Product Analysis,Y704-9764759)" is not offered.
    } yield ExitCode.Success
  }
}

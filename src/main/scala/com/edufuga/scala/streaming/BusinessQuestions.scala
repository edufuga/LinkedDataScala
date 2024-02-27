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

      // Business question: Find services without responsible department
      // 1. Obtain all services, irrespective of the organisation mapping
      // 2. Obtain the services within the organisation mapping.
      // 3. Return the difference (i.e. the services NOT found in the list of all entities).
      _ <- IO.println(s"Obtain all services, irrespective of the organisation mapping")
      services <- serviceDAO.readAll.compile.toList
      _ <- IO.println(services)
      _ <- IO.println(services.size)

      _ <- IO.println(s"Obtain the services within the organisation mapping.")
      // map + flatMap: Option[Organisation] -> Option[List[Service]] (instead of Option[List[List[Service]]])
      // sequence: Option[List[Service]] -> List[Option[Service]]
      // filter nonEmpty + (safe) get: List[Option[Service]] -> List[Service]
      organisationServices = organisation
        .map(_.departments.flatMap(_.services))
        .sequence[List, Service]
        .filter(_.nonEmpty)
        .map(_.get)
      _ <- IO.println(organisationServices)
      _ <- IO.println(organisationServices.size)

      _ <- IO.println(s"Return the difference (i.e. the services NOT provided by the organisation in any department).")
      serviceNotOfferedByTheOrganisation = services.diff(organisationServices)
      _ <- IO.println(serviceNotOfferedByTheOrganisation.map(s => (s.serviceName, s.id)))
      _ <- IO.println(serviceNotOfferedByTheOrganisation.size)
      // Answer: Only the product "(Product Analysis,Y704-9764759)" is not offered.


      // Business question: Find products without responsible department
      // 1. Obtain all products, irrespective of the organisation mapping
      // 2. Obtain the products within the organisation mapping.
      // 3. Return the difference (i.e. the products NOT found in the list of all entities).
      _ <- IO.println(s"Obtain all products, irrespective of the organisation mapping")
      products <- productDAO.readAll.compile.toList
      _ <- IO.println(products)
      _ <- IO.println(products.size) // 983

      _ <- IO.println(s"Obtain the products within the organisation mapping.")
      // map + flatMap: Option[Organisation] -> Option[List[Product]] (instead of Option[List[List[Product]]])
      // sequence: Option[List[Product]] -> List[Option[Product]]
      // filter nonEmpty + (safe) get: List[Option[Product]] -> List[Product]
      organisationProducts = organisation
        .map(_.departments.flatMap(_.products))
        .sequence[List, Product]
        .filter(_.nonEmpty)
        .map(_.get)
      _ <- IO.println(organisationProducts)
      _ <- IO.println(organisationProducts.size) // 49

      _ <- IO.println(s"Return the difference (i.e. the products NOT provided by the organisation in any department).")
      productNotOfferedByTheOrganisation = products.diff(organisationProducts)
      _ <- IO.println(productNotOfferedByTheOrganisation.map(p => (p.productName, p.id)))
      _ <- IO.println(productNotOfferedByTheOrganisation.size)
      // Answer: 934 products NOT offered by the organisation! (983 - 49 = 934).

      // TODO: Find related products (e.g. cluster by packaging size or weight)

      // TODO: Identify alternative experts that can take over responsibility
      // FIXME: What does this even mean? Responsibility for what?
      // Each Department has a list of employees.
      // An Employee is a product expert for something. This is the list of "ProductExpert" (or product EXPERIENCE).
      //   In other words: An Employee is "expert for several products".
      // Each Product and service has a "ProductManager". This is necessarily implies being a "ProductExpert".
      //
      // So, essentially we need to GROUP the employees (of a department) BY their Product Expertise ("ProductExpert").
      // "Who is a product expert for X?" -> List[Employee].
      // FIXME: Actually, a ProductManager is always an Email, which is contained in the Employee type.
      _ <- IO.println(s"Obtain the product experts within a department of the organisation.")

    } yield ExitCode.Success
  }
}

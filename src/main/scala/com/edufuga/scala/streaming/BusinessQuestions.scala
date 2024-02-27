package com.edufuga.scala.streaming

import cats.effect.{ExitCode, IO}
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.*

class BusinessQuestions(
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
    } yield ExitCode.Success
  }
}

package com.edufuga.scala.streaming

import cats.effect.{ExitCode, IO}
import com.edufuga.scala.entities.Manager
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.*

class BusinessQuestions(
  fullOrganisationDAO: FullOrganisationTypeLevelEffectfulDAO
) {
  def stream: IO[ExitCode] = {
    for {
      _ <- IO.println("Processing the full organisation. This includes resolving the linked products and services.")
      organisation <- fullOrganisationDAO.readAll
      _ <- IO.println(organisation)

      // Find services without a product manager
      // Wait a second, the "Product" and "Service" have a MANDATORY ProductManager.
      // As of now, this question makes no sense. It's not a valid question. The type system excludes it!
    } yield ExitCode.Success
  }
}

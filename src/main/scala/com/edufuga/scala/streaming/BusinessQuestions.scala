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
    } yield ExitCode.Success
  }
}

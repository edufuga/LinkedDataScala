package com.edufuga.scala.operations.entity.implementation.effectful.graph

import cats.effect.IO
import com.edufuga.scala.entities.FullOrganisation
import com.edufuga.scala.ogm.ObjectGraphMappings
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.FullOrganisationTypeLevelEffectfulDAO
import productdata.rdf.model.IOrganisation

import scala.util.{Failure, Success, Try}

sealed class FullOrganisationTypeLevelEffectfulGraphDAO(
  graph: () => IOrganisation, // XXX: When injecting this function, we need to pay special attention with the mutations!
) extends FullOrganisationTypeLevelEffectfulDAO {
  private def graphToObject(graph: IOrganisation): FullOrganisation =
    ObjectGraphMappings.OrganisationMappings.graphToObject(graph)

  override def readAll: IO[Option[FullOrganisation]] = {
    Try {
      val organisationGraph: IOrganisation = graph()
      val fullOrganisation: FullOrganisation = graphToObject(organisationGraph)
      // FIXME: We should optimize this into a lazy loading strategy, by wrapping the previous code inside an IO.
      fullOrganisation
    } match
      case Failure(exception) =>
        println("Error! " + exception.getMessage)
        IO.pure(None)
      case Success(organisation) => IO.pure(Some(organisation))
  }
}

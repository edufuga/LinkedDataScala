package com.edufuga.scala.operations.entity.implementation.effectful.graph

import cats.effect.IO
import com.edufuga.scala.entities.FullOrganisation
import com.edufuga.scala.ogm.ObjectGraphMappings
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.FullOrganisationTypeLevelEffectfulDAO
import productdata.rdf.model.IOrganisation

sealed class FullOrganisationTypeLevelEffectfulGraphDAO(
  graph: () => IOrganisation, // XXX: When injecting this function, we need to pay special attention with the mutations!
) extends FullOrganisationTypeLevelEffectfulDAO {
  private def graphToObject(graph: IOrganisation): FullOrganisation =
    ObjectGraphMappings.OrganisationMappings.graphToObject(graph)

  override def readAll: IO[Option[FullOrganisation]] = {
      val organisationGraph: IOrganisation = graph()
      val fullOrganisation: FullOrganisation = graphToObject(organisationGraph)
      // FIXME: We should optimize this into a lazy loading strategy, by wrapping the previous code inside an IO.
      IO.pure(Some(fullOrganisation))
  }
}

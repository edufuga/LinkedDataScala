package com.edufuga.scala.operations.entity.implementation.effectful.graph

import cats.effect.IO
import com.edufuga.scala.entities.FullOrganisation
import com.edufuga.scala.ogm.ObjectGraphMappings
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.FullOrganisationTypeLevelEffectfulDAO
import productdata.rdf.model.IOrganisation

/**
 * This DAO presents the "full organisation", using the organisation graph. It's just a wrapper of the API.
 *
 * ATTENTION: When injecting the organisation graph (evaluation), we need to pay special attention with the mutations
 * that may or may not happen during that evaluation. It's the task of the caller to ensure that there is no global
 * state being mutated, or _if this happens_, then it should happen in a controlled way. Otherwise we may end up
 * polluting the global model with unwanted or unforeseen duplicates.
 *
 * @param graph Organisation graph (with lazy evaluation)
 */
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

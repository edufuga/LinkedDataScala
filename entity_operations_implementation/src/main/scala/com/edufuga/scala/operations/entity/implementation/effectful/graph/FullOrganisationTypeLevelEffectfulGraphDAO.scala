package com.edufuga.scala.operations.entity.implementation.effectful.graph

import cats.effect.IO
import com.edufuga.scala.entities.FullOrganisation
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.FullOrganisationTypeLevelEffectfulDAO
import com.edufuga.scala.operations.entity.implementation.TechnologicalDetailTypes.TypeLevelEffectfulOptional
import productdata.rdf.model.IOrganisation

sealed class FullOrganisationTypeLevelEffectfulGraphDAO(
  graph: () => IOrganisation,
  graphToObject: IOrganisation => FullOrganisation
) extends FullOrganisationTypeLevelEffectfulDAO {
  override def readAll: IO[Option[FullOrganisation]] = {
    val organisationGraph: IOrganisation = graph()
    val fullOrganisation: FullOrganisation = graphToObject(organisationGraph)
    IO.pure(Some(fullOrganisation))
  }
}

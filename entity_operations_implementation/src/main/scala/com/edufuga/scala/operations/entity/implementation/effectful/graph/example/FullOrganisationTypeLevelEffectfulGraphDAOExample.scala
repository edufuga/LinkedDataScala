package com.edufuga.scala.operations.entity.implementation.effectful.graph.example

import cats.effect.{IO, IOApp}
import com.edufuga.scala.entities.FullOrganisation
import com.edufuga.scala.ogm.ObjectGraphMappings
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.FullOrganisationTypeLevelEffectfulDAO
import com.edufuga.scala.operations.entity.implementation.effectful.graph.FullOrganisationTypeLevelEffectfulGraphDAO
import productdata.rdf.model.IOrganisation

object FullOrganisationTypeLevelEffectfulGraphDAOExample extends IOApp {
  import cats.effect.ExitCode

  override def run(args: List[String]): IO[ExitCode] = {
    import com.edufuga.scala.ogm.example.ObjectConstructionExample

    val organisationObject: FullOrganisation = ObjectConstructionExample.organisation
    val organisationGraph: IOrganisation = ObjectGraphMappings.OrganisationMappings.objectToGraph(organisationObject)

    val organisationDAO: FullOrganisationTypeLevelEffectfulDAO = FullOrganisationTypeLevelEffectfulGraphDAO(
      graph = () => organisationGraph
    )

    for {
      organisation <- organisationDAO.readAll
      _ <- IO.println(s"[Example] $organisation")
    } yield ExitCode.Success
  }
}

package com.edufuga.scala.ogm.example

import org.eclipse.rdf4j.rio.{RDFFormat, Rio}
import productdata.global.util.GLOBAL
import com.edufuga.scala.entities.FullOrganisation
import com.edufuga.scala.ogm.ObjectOntologyMappings
import productdata.rdf.model.IOrganisation

object ObjectOntologyMappingExample extends App {
  val organisationObject: FullOrganisation = ObjectConstructionExample.organisation

  val organisationOntology: IOrganisation =
    ObjectOntologyMappings.OrganisationMappings.objectToOntology(organisationObject)

  serialize()

  // TODO: 'Transform' the IOrganisation back into a FullOrganisation. Compare the results.
  val organisationObjectConvertedFromGraph =
    ObjectOntologyMappings.OrganisationMappings.ontologyToObject(organisationOntology)

  println("Organisation object, converted from graph")
  println(organisationObjectConvertedFromGraph)

  private def serialize(): Unit = {
    Rio.write(GLOBAL.model, System.out, RDFFormat.TURTLE)
  }
}
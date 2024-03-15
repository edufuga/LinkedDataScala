package com.edufuga.scala.ogm.example

import org.eclipse.rdf4j.rio.{RDFFormat, Rio}
import productdata.global.util.GLOBAL

import com.edufuga.scala.entities.FullOrganisation
import com.edufuga.scala.ogm.ObjectOntologyMappings

object ObjectOntologyMappingExample extends App {
  val organisationObject: FullOrganisation = ObjectConstructionExample.organisation

  val organisationOntology = ObjectOntologyMappings.OrganisationMappings.objectToOntology(organisationObject)
  
  serialize()

  private def serialize(): Unit = {
    Rio.write(GLOBAL.model, System.out, RDFFormat.TURTLE)
  }
}
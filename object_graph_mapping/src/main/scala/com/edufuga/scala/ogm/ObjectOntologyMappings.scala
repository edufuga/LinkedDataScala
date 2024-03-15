package com.edufuga.scala.ogm

import com.edufuga.scala.entities._
import productdata.rdf.model._

object ObjectOntologyMappings {
  object OrganisationMappings extends ObjectOntologyMapping[FullOrganisation, IOrganisation] {
    override def objectToOntology(entity: FullOrganisation): IOrganisation = ???

    override def ontologyToObject(ontology: IOrganisation): FullOrganisation = ???
  }

  object DepartmentMappings extends ObjectOntologyMapping[FullDepartment, IDepartment] {
    override def objectToOntology(entity: FullDepartment): IDepartment = ???
    override def ontologyToObject(ontology: IDepartment): FullDepartment = ???
  }
}

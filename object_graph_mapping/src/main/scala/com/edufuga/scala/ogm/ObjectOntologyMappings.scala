package com.edufuga.scala.ogm

import com.edufuga.scala.entities as ent
import productdata.rdf.model as ont

object ObjectOntologyMappings {
  object OrganisationMappings extends ObjectOntologyMapping[ent.FullOrganisation, ont.IOrganisation] {
    override def objectToOntology(entity: ent.FullOrganisation): ont.IOrganisation = ???

    override def ontologyToObject(ontology: ont.IOrganisation): ent.FullOrganisation = ???
  }

  object DepartmentMappings extends ObjectOntologyMapping[ent.FullDepartment, ont.IDepartment] {
    override def objectToOntology(entity: ent.FullDepartment): ont.IDepartment = ???
    override def ontologyToObject(ontology: ont.IDepartment): ent.FullDepartment = ???
  }

  object ManagerMappings extends ObjectOntologyMapping[ent.Manager, ont.IManager] {
    override def objectToOntology(entity: ent.Manager): ont.IManager = ???
    override def ontologyToObject(ontology: ont.IManager): ent.Manager = ???
  }

  object ManagerMappings extends ObjectOntologyMapping[ent.Manager, ont.IManager] {
    override def objectToOntology(entity: ent.Manager): ont.IManager = ???

    override def ontologyToObject(ontology: ont.IManager): ent.Manager = ???
  }

  object EmployeeMappings extends ObjectOntologyMapping[ent.Employee, ont.Employee] {
    override def objectToOntology(entity: ent.Employee): ont.IEmployee = ???

    override def ontologyToObject(ontology: ont.IEmployee): ent.Employee = ???
  }
}

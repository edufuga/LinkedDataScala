package com.edufuga.scala.ogm

import com.edufuga.scala.entities as ent
import productdata.rdf.model as ont

object ObjectOntologyMappings {
  private val NAMESPACE: String = "https://github.com/edufuga/LinkedDataScala/2024/3/ProductData#"

  private def stringify(text: String) = text.replaceAll(" ", "_")

  // SMELL: Using an obviously stateful counter for setting the "instance IDs" of the ontology objects.
  //  A better solution would be to create the instance ID from a certain property of the objects themselves, but well.
  private class Counting(val countableThing: String) {
    private var counter: Integer = 1 // I hate my life.
    def count(): String = s"${countableThing}_${counter += 1}" // I hate my life.
  }

  object OrganisationMappings extends ObjectOntologyMapping[ent.FullOrganisation, ont.IOrganisation] {
    private val COUNTER = Counting("organisation")

    override def objectToOntology(entity: ent.FullOrganisation): ont.IOrganisation = {
      val ontology = ont.Organisation(NAMESPACE, COUNTER.count())
      entity.departments.foreach { department =>
        ontology.addDepartments(
          DepartmentMappings.objectToOntology(department)
        )
      }
      ontology
    }

    override def ontologyToObject(ontology: ont.IOrganisation): ent.FullOrganisation = ???
  }

  object DepartmentMappings extends ObjectOntologyMapping[ent.FullDepartment, ont.IDepartment] {
    override def objectToOntology(entity: ent.FullDepartment): ont.IDepartment = {
      val ontology = ont.Department(NAMESPACE, s"${entity.id}")

      ontology.setId(s"${entity.id}")
      ontology.setName(s"${entity.name}")
      ontology.addManager(ManagerMappings.objectToOntology(entity.manager))

      entity.employees.foreach { employee =>
        ontology.addEmployees(
          EmployeeMappings.objectToOntology(employee)
        )
      }

      entity.products.foreach { product =>
        ontology.addProducts(
          ProductMappings.objectToOntology(product)
        )
      }

      entity.services.foreach { service =>
        ontology.addServices(
          ServiceMappings.objectToOntology(service)
        )
      }

      ontology
    }
    override def ontologyToObject(ontology: ont.IDepartment): ent.FullDepartment = ???
  }

  object ManagerMappings extends ObjectOntologyMapping[ent.Manager, ont.IManager] {
    override def objectToOntology(entity: ent.Manager): ont.IManager = {
      val ontology = ont.Manager(NAMESPACE, stringify(s"${entity.name}"))
      ontology.setEmail(s"${entity.email}")
      ontology.setName(s"${entity.name}")
      ontology.setAddress(s"${entity.address}")
      ontology.setPhone(s"${entity.phone}")
      ontology
    }

    override def ontologyToObject(ontology: ont.IManager): ent.Manager = ???
  }

  object EmployeeMappings extends ObjectOntologyMapping[ent.Employee, ont.Employee] {
    override def objectToOntology(entity: ent.Employee): ont.IEmployee = ???

    override def ontologyToObject(ontology: ont.IEmployee): ent.Employee = ???
  }

  object ProductMappings extends ObjectOntologyMapping[ent.Product, ont.IProduct] {
    override def objectToOntology(entity: ent.Product): ont.IProduct = ???
    override def ontologyToObject(ontology: ont.IProduct): ent.Product = ???
  }

  object ServiceMappings extends ObjectOntologyMapping[ent.Service, ont.IService] {
    override def objectToOntology(entity: ent.Service): ont.IService = ???

    override def ontologyToObject(ontology: ont.IService): ent.Service = ???
  }
}

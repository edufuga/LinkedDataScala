package com.edufuga.scala.streaming

import productdata.rdf.model.*
import productdata.global.util.GLOBAL

import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.Rio

object GraphExample extends App {
  // Welcome to Imperative Programming with Mutations.
  // Welcome to the Open World of Knowledge Graphs.
  createTopology()
  serialize()

  private def createTopology(): Unit = {
    val ns = "http://www.example.com#"

    val organisation: IOrganisation = Organisation(ns, "o1")

    // Create Department for Organisation.
    val department: IDepartment = Department(ns, "d1")
    department.setId("73191")
    department.setName("Engineering")
    { // Create Manager for Department.
      val manager: IManager = Manager(ns, "ma1")
      manager.setName("Thomas Mueller")
      manager.setEmail("Thomas.Mueller@company.org")
      manager.setAddress("Karl-Liebknecht-Straße 885, 82003 Tettnang")
      manager.setPhone("+49-8200-38218301")

      department.addManager(manager)
    }
    // TODO: Add Employees to Department.

    // Create Product for Department _and_ Services
    val product: IProduct = Product(ns, "p1")
    product.setId("I241-8776317")
    product.setName("Strain Compensator") // I always find this name funny.
    product.setHeight(java.math.BigDecimal(12))
    product.setWidth(java.math.BigDecimal(68))
    product.setDepth(java.math.BigDecimal(15))
    product.setWeight(java.math.BigDecimal(8))
    product.setProductManager("Baldwin.Dirksen@company.org") // Good old Baldwin Dirksen.
    {
      val fortune: Money = Money(ns, "mo1")
      fortune.setCurrency("EUR")
      fortune.setMonetaryValue(0.5f)
      product.addPrice(fortune)
    }
    department.addProducts(product)

    {
      // Create Service for Department.
      val service: IService = Service(ns, "s1")
      service.setId("I241-8776317")
      service.setName("Component Confabulation")
      service.addProducts(product)
      service.setProductManager("Corinna.Ludwig@company.org")
      {
        val expensive: Money = Money(ns, "mo2")
        expensive.setCurrency("EUR")
        expensive.setMonetaryValue(0.5f)
        service.addPrice(expensive)
      }
      department.addServices(service)
    }


    organisation.addDepartments(department)
  }

  private def serialize(): Unit = {
    Rio.write(GLOBAL.model, System.out, RDFFormat.TURTLE)
  }
}

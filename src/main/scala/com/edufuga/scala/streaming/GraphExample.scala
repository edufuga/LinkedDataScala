package com.edufuga.scala.streaming

import productdata.rdf.model.*
import productdata.global.util.GLOBAL

import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.Rio

object GraphExample extends App {
  createTopology()
  serialize()

  private def createTopology(): Unit = {
    val ns = "https://github.com/edufuga/LinkedDataScala/2024/3/productdata#"

    val organisation: IOrganisation = Organisation(ns, "o1")

    // Create Department for Organisation.
    val department: IDepartment = Department(ns, "d1")
    department.setId("73191")
    department.setName("Engineering")
    { // Create Manager for Department.
      val manager: IManager = Manager(ns, "m1")
      manager.setName("Thomas Mueller")
      manager.setEmail("Thomas.Mueller@company.org")
      manager.setAddress("Karl-Liebknecht-Straße 885, 82003 Tettnang")
      manager.setPhone("+49-8200-38218301")

      department.addManager(manager)
    }

    // Create Product for Department _and_ Services
    val product: IProduct = Product(ns, "p1")
    //product.setName("Strain Compensator") // I always find this name funny. // TODO: Bigdecimal?? Error in ontology...
    product.setHeigth(java.math.BigDecimal(12))
    product.setWidth(java.math.BigDecimal(68))
    product.setDepth(java.math.BigDecimal(15))
    // product.setWeight(java.math.BigDecimal(8)) // TODO: Missing in the ontology?
    product.setProductManager("Baldwin.Dirksen@company.org") // Good old Baldwin Dirksen.
    //product.setPrice(Money(0.50, EUR)) // TODO: Missing in the ontology?
    department.addProducts(product)

    {
      // Create Service for Department.
      val service: IService = Service(ns, "s1")
      service.setId("I241-8776317")
      service.setName("Component Confabulation")
      service.addProducts(product)
      department.addServices(service)
    }


    organisation.addDepartments(department)
  }

  private def serialize(): Unit = {
    Rio.write(GLOBAL.model, System.out, RDFFormat.TURTLE)
  }
}

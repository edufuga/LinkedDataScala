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

    organisation.addDepartments(department)
  }

  private def serialize(): Unit = {
    Rio.write(GLOBAL.model, System.out, RDFFormat.TURTLE)
  }
}

package com.edufuga.scala.streaming

object DepartmentTypes {
  opaque type DepartmentId = String
  object DepartmentId {
    def apply(value: String): DepartmentId = value
  }

  opaque type DepartmentName = String

  object DepartmentName {
    def apply(value: String): DepartmentName = value
  }
}

import DepartmentTypes._
case class Department(
  id: DepartmentId,
  name: DepartmentName,
  // TODO: Manager, Employees, products, services
  manager: Manager,
  employees: List[Employee]
)

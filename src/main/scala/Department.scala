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

import DepartmentTypes.*
import ProductTypes.ProductId
import ServiceTypes.ServiceId

case class Department(
  id: DepartmentId,
  name: DepartmentName,
  manager: Manager,
  employees: List[Employee],
  productIds: List[ProductId],
  serviceIds: List[ServiceId]
)

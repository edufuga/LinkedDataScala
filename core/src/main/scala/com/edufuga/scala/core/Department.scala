package com.edufuga.scala.core

import DepartmentTypes.{DepartmentId, DepartmentName}
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
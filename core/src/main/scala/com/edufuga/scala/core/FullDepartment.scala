package com.edufuga.scala.core

import com.edufuga.scala.core.DepartmentTypes.{DepartmentId, DepartmentName}
import com.edufuga.scala.core.ProductTypes.ProductId
import com.edufuga.scala.core.ServiceTypes.ServiceId

case class FullDepartment(
  id: DepartmentId,
  name: DepartmentName,
  manager: Manager,
  employees: List[Employee],
  productIds: List[ProductId],
  serviceIds: List[ServiceId]
) extends Identifiable[DepartmentId]

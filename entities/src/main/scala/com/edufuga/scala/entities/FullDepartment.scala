package com.edufuga.scala.entities

import com.edufuga.scala.entities.DepartmentTypes.{DepartmentId, DepartmentName}

case class FullDepartment(
  id: DepartmentId,
  name: DepartmentName,
  manager: Manager,
  employees: List[Employee],
  products: List[Product],
  services: List[FullService]
) extends Identifiable[DepartmentId]

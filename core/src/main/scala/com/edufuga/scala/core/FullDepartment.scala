package com.edufuga.scala.core

import com.edufuga.scala.core.DepartmentTypes.{DepartmentId, DepartmentName}

case class FullDepartment(
  id: DepartmentId,
  name: DepartmentName,
  manager: Manager,
  employees: List[Employee],
  products: List[Product],
  services: List[Service]
) extends Identifiable[DepartmentId]

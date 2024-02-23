package com.edufuga.scala.entities

import EmployeeTypes.ProductExpert
import PersonTypes.{Address, Email, Name, Phone}

case class Employee(
  email: Email,
  name: Name,
  address: Option[Address],
  phone: Option[Phone],
  productExpert: List[ProductExpert]
)

package com.edufuga.scala.streaming

object EmployeeTypes {
  opaque type ProductExpert = String
  object ProductExpert {
    def apply(value: String): ProductExpert = value
  }
}

import PersonTypes._
import EmployeeTypes._

case class Employee(
  email: Email,
  name: Name,
  address: Address,
  phone: Phone,
  productExpert: List[ProductExpert]
)

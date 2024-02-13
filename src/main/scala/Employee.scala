package com.edufuga.scala.streaming

object EmployeeTypes {
  opaque type Email = String
  object Email {
    def apply(value: String): Email = value
  }

  opaque type Name = String
  object Name {
    def apply(value: String): Name = value
  }

  opaque type Address = String
  object Address {
    def apply(value: String): Address = value
  }

  opaque type Phone = String
  object Phone {
    def apply(value: String): Phone = value
  }

  opaque type ProductExpert = String
  object ProductExpert {
    def apply(value: String): ProductExpert = value
  }
}

import EmployeeTypes._
case class Employee(
  email: Email,
  name: Name,
  address: Address,
  phone: Phone,
  productExpert: List[ProductExpert]
)

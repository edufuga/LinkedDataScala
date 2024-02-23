package com.edufuga.scala.entities

object EmployeeTypes {
  opaque type ProductExpert = String

  object ProductExpert {
    def apply(value: String): ProductExpert = value
  }
}

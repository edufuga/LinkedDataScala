package com.edufuga.scala.core

object EmployeeTypes {
  opaque type ProductExpert = String

  object ProductExpert {
    def apply(value: String): ProductExpert = value
  }
}

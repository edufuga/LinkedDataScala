package com.edufuga.scala.core

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

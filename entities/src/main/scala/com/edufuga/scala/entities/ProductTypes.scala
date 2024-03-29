package com.edufuga.scala.entities

import com.edufuga.scala.entities.PersonTypes.Email

object ProductTypes {
  opaque type ProductId = String

  object ProductId {
    def apply(value: String): ProductId = value
  }

  opaque type ProductName = String

  object ProductName {
    def apply(value: String): ProductName = value
  }

  // This is a normal type alias. A "ProductManager" IS an email address.
  type ProductManager = Email

  object ProductManager {
    def apply(value: String): ProductManager = Email(value)
  }

  opaque type Height = Int

  object Height {
    def apply(value: Int): Height = value
  }

  opaque type Width = Int

  object Width {
    def apply(value: Int): Width = value
  }

  opaque type Weight = Int

  object Weight {
    def apply(value: Int): Weight = value
  }

  opaque type Depth = Int

  object Depth {
    def apply(value: Int): Depth = value
  }
}

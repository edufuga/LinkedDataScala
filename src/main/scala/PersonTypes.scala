package com.edufuga.scala.streaming

object PersonTypes {
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
}

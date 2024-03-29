package com.edufuga.scala.entities

object ServiceTypes {
  opaque type ServiceId = String

  object ServiceId {
    def apply(value: String): ServiceId = value
  }

  opaque type ServiceName = String

  object ServiceName {
    def apply(value: String): ServiceName = value
  }
}

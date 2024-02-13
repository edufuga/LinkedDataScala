package com.edufuga.scala.streaming

import ServiceTypes._

object ServiceTypes {
  opaque type ServiceId = String
  object ServiceId {
    def apply(value: String): ServiceId = value
  }

  opaque type ServiceName = String
  object ServiceName {
    def apply(value: String): ServiceName = value
  }

  opaque type ProductManager = String
  object ProductManager {
    def apply(value: String): ProductManager = value
  }

  opaque type ProductId = String

  object ProductId {
    def apply(value: String): ProductId = value
  }
}

case class Service(
  serviceId: ServiceId,
  serviceName: ServiceName,
  products: List[ProductId],
  productManager: ProductManager,
  price: Money
)

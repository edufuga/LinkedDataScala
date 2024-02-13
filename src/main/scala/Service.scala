package com.edufuga.scala.streaming

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

import ServiceTypes._
import ProductTypes.{ProductId, ProductManager}

case class Service(
  serviceId: ServiceId,
  serviceName: ServiceName,
  products: List[ProductId],
  productManager: ProductManager,
  price: Money
)

package com.edufuga.scala.streaming

import ServiceTypes._
import ProductTypes.{ProductId, ProductManager}

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

case class Service(
  serviceId: ServiceId,
  serviceName: ServiceName,
  products: List[ProductId],
  productManager: ProductManager,
  price: Money
)

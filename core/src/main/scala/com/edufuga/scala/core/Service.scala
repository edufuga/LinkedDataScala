package com.edufuga.scala.core

import ProductTypes.{ProductId, ProductManager}
import ServiceTypes.{ServiceId, ServiceName}

case class Service(
  serviceId: ServiceId,
  serviceName: ServiceName,
  products: List[ProductId],
  productManager: ProductManager,
  price: Money
)

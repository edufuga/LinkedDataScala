package com.edufuga.scala.entities

import ProductTypes.{ProductId, ProductManager}
import ServiceTypes.{ServiceId, ServiceName}

case class Service(
  id: ServiceId,
  serviceName: ServiceName,
  products: List[ProductId],
  productManager: ProductManager, // Same as Email
  price: Money
) extends Identifiable[ServiceId]

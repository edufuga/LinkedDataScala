package com.edufuga.scala.entities

import ProductTypes.{ProductId, ProductManager}
import ServiceTypes.{ServiceId, ServiceName}

// TODO: Instead of Service, it needs to be a "FullService". It needs the list of full products, not just the IDs.
case class Service(
  id: ServiceId,
  serviceName: ServiceName,
  products: List[ProductId],
  productManager: ProductManager, // Same as Email
  price: Money
) extends Identifiable[ServiceId]

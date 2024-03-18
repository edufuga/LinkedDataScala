package com.edufuga.scala.entities

import com.edufuga.scala.entities.ProductTypes.ProductManager
import com.edufuga.scala.entities.ServiceTypes.{ServiceId, ServiceName}

case class FullService(
  id: ServiceId,
  serviceName: ServiceName,
  products: List[Product],
  productManager: ProductManager, // Same as Email
  price: Money
) extends Identifiable[ServiceId]

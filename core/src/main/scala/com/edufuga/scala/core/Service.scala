package com.edufuga.scala.core

import com.edufuga.scala.core.ProductTypes.{ProductId, ProductManager}
import com.edufuga.scala.core.ServiceTypes.{ServiceId, ServiceName}

case class Service(
  serviceId: ServiceId,
  serviceName: ServiceName,
  products: List[ProductId],
  productManager: ProductManager,
  price: Money
)

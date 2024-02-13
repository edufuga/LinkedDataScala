package com.edufuga.scala.streaming

case class Service(
  serviceId: String, // TODO: Use value types or type aliases
  serviceName: String, // TODO: Use value types or type aliases
  products: List[String], // TODO: Use value types or type aliases
  productManager: String,  // TODO: Use value types or type aliases
  price: Money
)

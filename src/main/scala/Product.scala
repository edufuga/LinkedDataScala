package com.edufuga.scala.streaming

case class Product(
  productId: String, // TODO: Use value types or type aliases
  productName: String, // TODO: Use value types or type aliases
  height: Int,
  width: Int,
  depth: Int,
  weight: Int,
  productManager: String, // TODO: Use value types or type aliases
  price: Money
)

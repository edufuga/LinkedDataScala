package com.edufuga.scala.entities

import com.edufuga.scala.entities.ProductTypes.*

case class Product(
  id: ProductId,
  productName: ProductName,
  height: Height,
  width: Width,
  depth: Depth,
  weight: Weight,
  productManager: ProductManager, // Same as Email // TODO: Use Employee type. // Replace ProductManager with Employee.
  price: Money
) extends Identifiable[ProductId]

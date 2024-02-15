package com.edufuga.scala.core

import com.edufuga.scala.core.ProductTypes.*

case class Product(
  productId: ProductId,
  productName: ProductName,
  height: Height,
  width: Width,
  depth: Depth,
  weight: Weight,
  productManager: ProductManager,
  price: Money
)

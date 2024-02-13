package com.edufuga.scala.streaming

import ProductTypes._

object ProductTypes {
  opaque type ProductId = String
  object ProductId {
    def apply(value: String): ProductId = value
  }

  opaque type ProductName = String
  object ProductName {
    def apply(value: String): ProductName = value
  }

  opaque type ProductManager = String
  object ProductManager {
    def apply(value: String): ProductManager = value
  }


  opaque type Height = Int
  object Height {
    def apply(value: Int): Height = value
  }

  opaque type Width = Int
  object Width {
    def apply(value: Int): Width = value
  }

  opaque type Weight = Int
  object Weight {
    def apply(value: Int): Weight = value
  }

  opaque type Depth = Int
  object Depth {
    def apply(value: Int): Depth = value
  }
}

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

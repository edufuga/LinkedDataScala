package com.edufuga.scala.streaming

object Parsers {
  def product(line: String): Product = {
    val Patterns.Products.line(id, name, height, width, depth, weight, manager, price) = line
    Product(
      productId = id,
      productName = name,
      height = height,
      width = width,
      depth = depth,
      weight = weight,
      productManager = manager,
      price = price
    )
  }

  def main(args: Array[String]): Unit = {
    val line = "I241-8776317,Strain Compensator,12,68,15,8,Baldwin.Dirksen@company.org,\"0,50 EUR\""
    val product: Product = Parsers.product(line)
    println(product)
  }
}

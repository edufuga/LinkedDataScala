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

  def service(line: String): Service = {
    val Patterns.Services.line(id, name, products, productManager, price) = line
    Service(
      serviceId = id,
      serviceName = name,
      products = products,
      productManager = productManager,
      price = price
    )
  }

  def main(args: Array[String]): Unit = {
    val productLine = "I241-8776317,Strain Compensator,12,68,15,8,Baldwin.Dirksen@company.org,\"0,50 EUR\""
    val product: Product = Parsers.product(productLine)
    println(product)

    val serviceLine = "Y704-9764759,Product Analysis,\"O491-3823912, I965-1821441, Z655-3173353, U733-5722614, K411-1729714\",Lambert.Faust@company.org,\"748,40 EUR\""
    val service: Service = Parsers.service(serviceLine)
    println(service)
  }
}

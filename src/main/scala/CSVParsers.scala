package com.edufuga.scala.streaming

import ServiceTypes._
import ProductTypes._
import Patterns.{decimalNumber, word}
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

object CSVParsers {
  private val quotedPriceAndCurrency: Regex = s"\"($decimalNumber)\\s($word)\"".r

  private def money(price: String): Option[Money] =
    Try {
      val quotedPriceAndCurrency(value, currency) = price
      Money.mkMoney(value, currency)
    }.toOption.flatten

  private def productList(productLine: String): List[ProductId] =
    productLine
      .replaceAll("\"", "")
      .split(",")
      .map(_.trim)
      .map(ProductId.apply)
      .toList

  def product(line: String): Option[Product] =
    Try {
      val Patterns.Products.line(id, name, height, width, depth, weight, manager, price) = line
      for {
        money <- money(price).toList // This is just here to avoid a weird overload 'error' (bug?) with Option.
        product = Product(
          productId = ProductId(id),
          productName = ProductName(name),
          height = Height(height.toInt),
          width = Width(width.toInt),
          depth = Depth(depth.toInt),
          weight = Weight(weight.toInt),
          productManager = ProductManager(manager),
          price = money
        )
      } yield product
    } match {
      case Failure(_) =>
        println(s"This product line contains an error: '$line'.")
        None
      case Success(value) => value.headOption
    }

  def service(line: String): Option[Service] =
    Try {
      val Patterns.Services.line(id, name, products, productManager, price) = line
      for {
        money <- money(price).toList // This is just here to avoid a weird overload 'error' (bug?) with Option.
        service = Service(
          serviceId = ServiceId(id),
          serviceName = ServiceName(name),
          products = productList(products),
          productManager = ProductManager(productManager),
          price = money
        )
      } yield service
    } match {
      case Failure(_) =>
        println(s"This service line contains an error: '$line'.")
        None
      case Success(value) => value.headOption
    }

  def main(args: Array[String]): Unit = {
    val productLine = "I241-8776317,Strain Compensator,12,68,15,8,Baldwin.Dirksen@company.org,\"0,50 EUR\""
    val product: Option[Product] = CSVParsers.product(productLine)
    println(product)

    val serviceLine = "Y704-9764759,Product Analysis,\"O491-3823912, I965-1821441, Z655-3173353, U733-5722614, K411-1729714\",Lambert.Faust@company.org,\"748,40 EUR\""
    val service: Option[Service] = CSVParsers.service(serviceLine)
    println(service)

    val errorProductLine = "E267-7496794,Crystal Rheostat,34,47,11,6,Frau.Irmalinda‚Äò.Becker@company.org,\"1,11 EUR\""
    println(CSVParsers.product(errorProductLine))
  }
}

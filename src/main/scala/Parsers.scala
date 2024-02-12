package com.edufuga.scala.streaming

import com.edufuga.scala.streaming.Patterns.{decimalNumber, word}

import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

object Parsers {
  private val quotedPriceAndCurrency: Regex = s"\"($decimalNumber)\\s($word)\"".r
  def money(price: String): Option[Money] = {
    val quotedPriceAndCurrency(value, currency) = price
    Money.mkMoney(value, currency)
  }

  val first: Option[Int] = Some(2)
  val second: Option[Int] = Some(3)
  val result: Option[String] =  for {
    f <- first
    s <- second
  } yield {
    s"Inputs are $f and $s "
  }

  def product(line: String): Option[Product] = {
    Try {
      val Patterns.Products.line(id, name, height, width, depth, weight, manager, price) = line
      for {
        money <- money(price).toList // This is just here to avoid a weird overload 'error' (bug?) with Option.
        product = Product(
          productId = id,
          productName = name,
          height = height.toInt,
          width = width.toInt,
          depth = depth.toInt,
          weight = weight.toInt,
          productManager = manager,
          price = money
        )
      } yield product
    } match {
      case Failure(_) =>
        println(s"This product line contains an error: '$line'.")
        None
      case Success(value) => value.headOption
    }
  }

  def service(line: String): Option[Service] = {
    Try {
      val Patterns.Services.line(id, name, products, productManager, price) = line
      for {
        money <- money(price).toList // This is just here to avoid a weird overload 'error' (bug?) with Option.
        service = Service(
          serviceId = id,
          serviceName = name,
          products = products,
          productManager = productManager,
          price = money
        )
      } yield service
    } match {
      case Failure(_) =>
        println(s"This service line contains an error: '$line'.")
        None
      case Success(value) => value.headOption
    }
  }

  def main(args: Array[String]): Unit = {
    val productLine = "I241-8776317,Strain Compensator,12,68,15,8,Baldwin.Dirksen@company.org,\"0,50 EUR\""
    val product: Option[Product] = Parsers.product(productLine)
    println(product)

    val serviceLine = "Y704-9764759,Product Analysis,\"O491-3823912, I965-1821441, Z655-3173353, U733-5722614, K411-1729714\",Lambert.Faust@company.org,\"748,40 EUR\""
    val service: Option[Service] = Parsers.service(serviceLine)
    println(service)

    val errorProductLine = "E267-7496794,Crystal Rheostat,34,47,11,6,Frau.Irmalinda‚Äò.Becker@company.org,\"1,11 EUR\""
    println(Parsers.product(errorProductLine))
  }
}

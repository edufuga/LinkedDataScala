package com.edufuga.scala.data.parsers.csv

import com.edufuga.scala.core.*
import com.edufuga.scala.core.ProductTypes.*
import com.edufuga.scala.core.ServiceTypes.*
import com.edufuga.scala.data.patterns.regex.RegexPatterns
import com.edufuga.scala.data.patterns.regex.RegexPatterns.{decimalNumber, word}

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
      val RegexPatterns.Products.line(id, name, height, width, depth, weight, manager, price) = line
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
      val RegexPatterns.Services.line(id, name, products, productManager, price) = line
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
}

package com.edufuga.scala.entity.parsers.csv

import com.edufuga.scala.entity.patterns.regex.RegexPatterns
import com.edufuga.scala.entity.patterns.regex.RegexPatterns.{decimalNumber, word}
import com.edufuga.scala.entities.*
import com.edufuga.scala.entities.ProductTypes.*
import com.edufuga.scala.entities.ServiceTypes.*

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
          id = ProductId(id),
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
        //println(s"This product line contains an error: '$line'.")
        None
      case Success(value) => value.headOption
    }

  def service(line: String): Option[Service] =
    Try {
      val RegexPatterns.Services.line(id, name, products, productManager, price) = line
      for {
        money <- money(price).toList // This is just here to avoid a weird overload 'error' (bug?) with Option.
        service = Service(
          id = ServiceId(id),
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

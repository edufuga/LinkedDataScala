package com.edufuga.scala.streaming

import scala.util.matching.Regex

object Patterns {
  val start: Regex = "^".r
  val end: Regex = "$".r

  // TODO: Separate it further into auxiliary subcategories (for the IDs, etc.).
  val digit: Regex = "[0-9]".r
  val character: Regex = "[A-Za-z]".r
  val characterOrDigit: Regex = "[A-Za-z0-9]".r
  val number: Regex = s"$digit+".r
  val word: Regex = s"$character+".r
  val numericalWord: Regex = s"$characterOrDigit+".r

  // TODO: Include uppercase letter at the beginning of each word?
  val words: Regex = s"(?:$word+)(?:\\s(?:$word))*".r           // possibly several words, separated by whitespaces.
  val numericalWords: Regex = s"(?:$numericalWord+)(?:\\s(?:$numericalWord))*".r

  def digits(cardinality: Int): Regex = s"$digit{$cardinality}".r

  val decimalNumber: Regex = s"$number,$number".r
  val price: Regex = s"$decimalNumber\\s$word".r
  val quotedPrice: Regex = s"\"$price\"".r

  val email: Regex = s"$numericalWords(?:\\.$numericalWords)+@$numericalWords(?:\\.$numericalWords)+".r

  private val idPrefix: Regex = s"$character${digits(3)}".r
  private val idSuffix: Regex = s"${digits(7)}".r
  val id: Regex = s"$idPrefix-$idSuffix".r
  val ids: Regex = s"(?:$id+)(?:,\\s(?:$id))*".r // list of comma separated IDs
  val quotedIds: Regex = s"\"$ids\"".r         // list of comma separated IDs within a pair of quotes

  object Product {
    val productId: Regex = id
    val productName: Regex = words
    val height: Regex = number
    val width: Regex = number
    val depth: Regex = number
    val weight: Regex = number
    val productManager: Regex = email
    val price: Regex = quotedPrice
  }

  object Service {
    val serviceId: Regex = id
    val serviceName: Regex = words
    val products: Regex = quotedIds
    val productManager: Regex = email
    val price: Regex = quotedPrice
  }

  object Products {
    val line: Regex = s"$start(${Product.productId}),(${Product.productName}),(${Product.height}),(${Product.width}),(${Product.depth}),(${Product.weight}),(${Product.productManager}),(${Product.price})$end".r
  }

  object Services {
    val line: Regex = s"$start(${Service.serviceId}),(${Service.serviceName}),(${Service.products}),(${Service.productManager}),(${Service.price})$end".r
  }

  def main(args: Array[String]): Unit = {
    val productsLine = "I241-8776317,Strain Compensator,12,68,15,8,Baldwin.Dirksen@company.org,\"0,50 EUR\""
    val servicesLine = "Y704-9764759,Product Analysis,\"O491-3823912, I965-1821441, Z655-3173353, U733-5722614, K411-1729714\",Lambert.Faust@company.org,\"748,40 EUR\""

    println(s"Products line: '$productsLine'")
    println(s"Services line: '$servicesLine'")

    println()

    val Patterns.Products.line(i,n,h,w,d,wg,m,p) = productsLine
    println("Product line from CSV")
    println("---------------------")
    println(i)
    println(n)
    println(h)
    println(w)
    println(d)
    println(wg)
    println(m)
    println(p)

    println()

    val Patterns.Services.line(si, sn, sp, sm, spr) = servicesLine
    println("Service line from CSV")
    println("---------------------")
    println(si)
    println(sn)
    println(sp)
    println(sm)
    println(spr)
  }
}

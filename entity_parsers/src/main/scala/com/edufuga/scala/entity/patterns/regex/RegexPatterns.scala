package com.edufuga.scala.entity.patterns.regex

import scala.util.matching.Regex

object RegexPatterns {
  val start: Regex = "^".r
  val end: Regex = "$".r

  val digit: Regex = "[0-9]".r
  val character: Regex = "[A-Za-z]".r
  val characterOrHyphen: Regex = "[A-Z\\-a-z]".r
  val characterOrDigit: Regex = "[A-Za-z0-9]".r
  val number: Regex = s"$digit+".r
  val word: Regex = s"$characterOrHyphen+".r
  val numericalWord: Regex = s"$characterOrDigit+".r

  val words: Regex = s"(?:$word+)(?:\\s(?:$word))*".r
  val numericalWords: Regex = s"(?:$numericalWord+)(?:\\s(?:$numericalWord))*".r

  def digits(cardinality: Int): Regex = s"$digit{$cardinality}".r

  val decimalNumber: Regex = s"$number,$number".r
  val price: Regex = s"$decimalNumber\\s$word".r
  val quotedPrice: Regex = s"\"$price\"".r

  val email: Regex = s"$numericalWords(?:\\.$numericalWords)+@$numericalWords(?:\\.$numericalWords)+".r

  private val idPrefix: Regex = s"$character${digits(3)}".r
  private val idSuffix: Regex = s"${digits(7)}".r
  val id: Regex = s"$idPrefix-$idSuffix".r
  val ids: Regex = s"(?:$id+)(?:,\\s(?:$id))*".r                // list of comma separated IDs
  val quotedIds: Regex = s"\"$ids\"".r                          // list of comma separated IDs within a pair of quotes

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
    import RegexPatterns.Product as P
    val line: Regex = s"$start(${P.productId}),(${P.productName}),(${P.height}),(${P.width}),(${P.depth}),(${P.weight}),(${P.productManager}),(${P.price})$end".r
  }

  object Services {
    import RegexPatterns.Service as S
    val line: Regex = s"$start(${S.serviceId}),(${S.serviceName}),(${S.products}),(${S.productManager}),(${S.price})$end".r
  }
}

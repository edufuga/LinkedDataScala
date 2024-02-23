package com.edufuga.scala.entities

import java.util.Currency
import scala.util.Try

case class Money(
  value: Float,
  currency: Currency
)

object Money {
  def mkMoney(textualValue: String, textualCurrency: String): Option[Money] = {
    Try {
      Money(
        value = textualValue.replace(',', '.').toFloat,
        Currency.getInstance(textualCurrency))
    }.toOption
  }

  def main(args: Array[String]): Unit = {
    println(mkMoney("0,50", "EUR"))
  }
}
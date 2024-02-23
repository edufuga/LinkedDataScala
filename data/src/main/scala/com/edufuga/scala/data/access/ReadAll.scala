package com.edufuga.scala.data.access

/**
 * Read interface.
 *
 * @tparam O Type of the outputted data, independent on the form in which it is returned (optional, IO, Streaming, etc.)
 */
trait ReadAll[+O, +S[+_]] {
  def readAll: S[O]
}

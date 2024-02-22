package com.edufuga.scala.data.access

/**
 * Interface for reading (finding) an entry by its ID.
 *
 * @tparam I Type of the ID.
 * @tparam O Type of the outputted data, independent on the form in which it is returned (optional, IO, Streaming, etc.)
 */
trait ReadById[I, +O] {
  def readById(id: => I): O
}

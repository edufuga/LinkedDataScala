package com.edufuga.scala.data.access

/**
 * Interface for reading (finding) entries by their IDs.
 *
 * @tparam I Type of the ID.
 * @tparam O Type of the outputted data, independent on the form in which it is returned (optional, IO, Streaming, etc.)
 */
trait ReadByIds[I, +O, +S[+_]] {
  def readByIds(ids: => Seq[I]): S[O]
}

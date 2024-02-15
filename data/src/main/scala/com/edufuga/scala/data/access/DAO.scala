package com.edufuga.scala.data.access

/**
 * Generic DAO interface.
 *
 * @tparam I Type of the ID.
 * @tparam O Type of the outputted data, independent on the form in which it is returned (optional, IO, Streaming, etc.)
 */
trait DAO[I, +O] extends ReadAll[O], ReadById[I, O]

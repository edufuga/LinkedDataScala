package com.edufuga.scala.data.access

/**
 * Generic DAO interface.
 * 
 * The DAO interface has two type parameters: 'I' for the Id type, and '+O' for the output type (this is the type of the
 * returned or processed results, i.e. the type of the entities on which the DAO works).
 *
 * The plus sign '+' in '+O' indicates the covariance of that type parameter 'O'.
 * This is in accordance to (and limited/dictated by) the trait 'DAO[I, +O]'.
 * The output type has to be covariant, which is a general rule of the type system (i.e. parameters are contravariant,
 * return types are covariant). We don't need to fully understand this, it only needs to be consistent with the
 * interface declaration (= the type system tells how to do it).
 *
 * @tparam I Type of the ID.
 * @tparam O Type of the outputted data, independent on the form in which it is returned (optional, IO, Streaming, etc.)
 */
trait DAO[I, +O] extends ReadAll[O], ReadById[I, O]

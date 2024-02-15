package com.edufuga.scala.data.access

/**
 *
 * Generic effectful DAO interface.
 *
 * An effectful DAO makes use of an effect monad (of type F), which is used to indicate the presence of side-effecting
 * logic. Instead of having side effects in the code, the effect type F represents them as an own object, which is then
 * evaluated (executed) in a different place of the program flow; normally, this is done in the entry point to the
 * program.
 *
 * @tparam I Type of the ID.
 * @tparam F Effectful type (e.g. IO from Cats Effect). Essentially, this is just a wrapper around the object of type O.
 * @tparam O Type of the outputted data, independent on the form in which it is returned (optional, IO, Streaming, etc.)
 * @tparam C Type of the 'channel', which wraps the returned objects of type O, themselves wrapped by the effect type F.
 */
trait EffectfulDAO[I, C[+_[_], +_], +F[_], +O] extends DAO[I, C[F, O]]

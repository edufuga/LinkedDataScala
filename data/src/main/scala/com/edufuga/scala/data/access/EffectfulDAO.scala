package com.edufuga.scala.data.access

/**
 *
 * Generic effectful DAO interface.
 *
 * An effectful DAO makes use of an effect monad (of type F), which is used to indicate the presence of side-effecting
 * logic. Instead of having side effects in the code, the effect type F represents them as an own object, which is then
 * evaluated (executed) in a different place of the program flow; normally, this is done in the entry point to the
 * program.
 **
 * The effect type F is a higher-kinded type with one type parameter. This type parameter is the '_' in '+F[+_]'.
 * We don't need a specific letter (type parameter) for it, so we just use the anonymous '_' to indicate that there is
 * only one type parameter within the effect type F.
 *
 * The plus signs '+' in '+F[+_]' indicate the covariance of the type parameter 'F' and of its own type parameter '_'.
 * This is in accordance to (and limited/dictated by) the trait 'DAO[I, +O]'. The output type has to be covariant, which
 * is a general rule of the type system (i.e. parameters are contravariant, return types are covariant). We don't need
 * to  fully understand this, it only needs to be consistent with the interface declaration (= the type system tells how
 * to do it).
 *
 * @tparam I Type of the ID.
 * @tparam F Effectful type (e.g. IO from Cats Effect). Essentially, this is just a wrapper around the object of type O.
 * @tparam O Type of the outputted data, independent on the form in which it is returned (optional, IO, Streaming, etc.)
 */
trait EffectfulDAO[I, +F[+_], O] extends DAO[I,5]

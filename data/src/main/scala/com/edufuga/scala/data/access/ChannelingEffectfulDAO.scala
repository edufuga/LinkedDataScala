package com.edufuga.scala.data.access

/**
 *
 * Generic channeling effectful DAO interface.
 *
 * An effectful DAO makes use of an effect monad (of type F), which is used to indicate the presence of side-effecting
 * logic. Instead of having side effects in the code, the effect type F represents them as an own object, which is then
 * evaluated (executed) in a different place of the program flow; normally, this is done in the entry point to the
 * program.
 *
 * A channeling DAO makes use of a channel (of type C), which is used as a means for transporting the results.
 * The channel type C is not just a generic type, but also a higher-kinded type. Concretely, it has the two type
 * parameters F and O, corresponding to the effect type (F) and th object type (O). This is written 'C[F, O]'.
 *
 * In a similar fashion, the effect type F is also a higher-kinded type with one type parameter. This type parameter is
 * the '_' in '+F[+_]'. We don't need a specific letter (type parameter) for it, so we just use the anonymous '_' to
 * indicate that there is only one type parameter within the effect type F. We also find the underscores in the
 * 'C[+_[_], +_]'.
 *
 * The plus sign '+' in '+O' and '+F[+_]' only indicates the covariance of those type parameters 'O' and 'F'.
 * This is in accordance to (and limited/dictated by) the trait 'DAO[I, +O]'. The output type has to be covariant, which
 * is a general rule of the type system (i.e. parameters are contravariant, return types are covariant). We don't need
 * to  fully understand this, it only needs to be consistent with the interface declaration (= the type system tells how
 * to do it).
 *
 * @tparam I Type of the ID.
 * @tparam F Effectful type (e.g. IO from Cats Effect). Essentially, this is just a wrapper around the object of type O.
 * @tparam O Type of the outputted data, independent on the form in which it is returned (optional, IO, Streaming, etc.)
 * @tparam C Type of the 'channel', which wraps the returned objects of type O, themselves wrapped by the effect type F.
 */
trait ChannelingEffectfulDAO[I, C[+_[_], +_], +F[+_], O] extends DAO[I, C[F,O]]

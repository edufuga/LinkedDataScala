package com.edufuga.scala.data.access.streamed

import com.edufuga.scala.data.access.ChannelingEffectfulDAO
import fs2.Stream

/**
 *
 * Generic streaming effectful DAO interface.
 *
 * An effectful DAO makes use of an effect monad (of type F), which is used to indicate the presence of side-effecting
 * logic. Instead of having side effects in the code, the effect type F represents them as an own object, which is then
 * evaluated (executed) in a different place of the program flow; normally, this is done in the entry point to the
 * program.
 *
 * A streaming DAO makes use of a fs2 stream (of type 'Stream[+F[_], +O]'), which is used as the concrete channel, i.e.
 * as the means of transporting the results. The Stream type is a concrete implementation from fs2 (functional streams
 * for Scala). Under the hood, it has the two type parameters F and O, corresponding to the effect type (F) and the
 * object type (O).
 *
 * The plus sign '+' in '+O' and '+F[+_]' only indicates the covariance of those type parameters 'O', 'F' and '_'.
 * This is in accordance to (and limited/dictated by) the trait 'ChannelingEffectfulDAO[I, C[+_[_], +_], +F[+_], O]'.
 * The output type has to be covariant, which is a general rule of the type system (i.e. parameters are contravariant,
 * return types are covariant). We don't need to fully understand this, it only needs to be consistent with the
 * interface declaration (= the type system tells how to do it).
 *
 * @tparam F Effectful type (e.g. IO from Cats Effect). Essentially, this is just a wrapper around the object of type O.
 * @tparam O Type of the outputted data, independent on the form in which it is returned (optional, IO, Streaming, etc.)
 */
trait StreamingDAO[Id, +F[+_], +O] extends ChannelingEffectfulDAO[Id, Stream, F, O]

package com.edufuga.scala.operations.entity.implementation.streamed

import cats.effect.IO
import com.edufuga.scala.entities.Identifiable
import com.edufuga.scala.operations.entity.implementation.TechnologicalDetailTypes.{TypeLevelEffectfulOptional, TypeLevelEffectfulStream}
import com.edufuga.scala.operations.DAO
import fs2.Stream

/**
 *
 * Streaming DAO interface using fs2 Streams and the IO monad from Cats Effect. Both are parts of the TypeLevel stack.
 *
 * A streaming DAO makes use of a fs2 stream (of type 'Stream[+F[_], +O]'), which is used as the concrete channel, i.e.
 * as the means of transporting the results. The Stream type is a concrete implementation from fs2 (functional streams
 * for Scala). Under the hood, it has the two type parameters F and O, corresponding to the effect type (F) and the
 * object type (O).
 *
 * The plus sign '+' in '+O' only indicate the covariance of the type parameter 'O'.
 * This is in accordance to (and limited/dictated by) the trait 'StreamingEffectfulDAO[Id, +F[+_], +O]'.
 * The output type has to be covariant, which is a general rule of the type system (i.e. parameters are contravariant,
 * return types are covariant). We don't need to fully understand this, it only needs to be consistent with the
 * interface declaration (= the type system tells how to do it).
 *
 * @tparam Id Type of the ID of the outputted entity.
 * @tparam O Type of the outputted data, independent on the form in which it is returned (optional, IO, Streaming, etc.)
 */
trait StreamingWithIODAO[Id, +O <: Identifiable[Id]] extends DAO[Id, O, TypeLevelEffectfulOptional, TypeLevelEffectfulStream] {
  override def readById(id: => Id): IO[Option[O]] = readAll.find(_.id.equals(id)).compile.last
  override def readByIds(ids: => Seq[Id]): Stream[IO, O] = readAll.filter { found => ids.contains(found.id) }
}

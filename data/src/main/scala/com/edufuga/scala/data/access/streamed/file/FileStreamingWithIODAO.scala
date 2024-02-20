package com.edufuga.scala.data.access.streamed.file

import cats.effect.IO
import com.edufuga.scala.data.access.ops.{FileOps, StreamOps}
import com.edufuga.scala.core.Identifiable
import com.edufuga.scala.data.access.streamed.StreamingWithIODAO
import fs2.Stream
import fs2.io.file.Files

/**
 * 
 * File-based streaming DAO interface using fs2 Streams and the IO monad from Cats Effect. Both are parts of the
 * TypeLevel stack.
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
 * @tparam O Type of the outputted data, independent on the form in which it is returned (optional, IO, Streaming, etc.)
 */
class FileStreamingWithIODAO[Id, +O <: Identifiable[Id]](file: String, entityParser: String => Option[O])
  extends StreamingWithIODAO[Id, O] {
  override def readAll: Stream[IO, O] =
    Files[IO].readAll(FileOps.pathOf(file))
      .through(
        StreamOps.entitiesPipe(
          entityParser
        )
      )
}

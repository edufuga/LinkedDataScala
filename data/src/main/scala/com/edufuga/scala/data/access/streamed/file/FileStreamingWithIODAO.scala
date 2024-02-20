package com.edufuga.scala.data.access.streamed.file

import cats.effect.IO
import com.edufuga.scala.data.access.ops.{FileOps, StreamOps}
import com.edufuga.scala.core.Identifiable
import com.edufuga.scala.data.access.streamed.StreamingWithIODAO
import fs2.Stream
import fs2.io.file.Files

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

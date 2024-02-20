package com.edufuga.scala.data.access.streamed.file

import cats.effect.IO
import com.edufuga.scala.core.Service
import com.edufuga.scala.data.access.ops.{FileOps, StreamOps}
import com.edufuga.scala.data.access.streamed.ServiceStreamingWithIODAO
import com.edufuga.scala.data.parsers.csv.CSVParsers
import fs2.Stream
import fs2.io.file.Files

case class FileServiceStreamingWithIODAO(file: String) extends ServiceStreamingWithIODAO {
  override def readAll: Stream[IO, Service] =
    Files[IO].readAll(FileOps.pathOf(file))
      .through(
        StreamOps.entitiesParser(
          CSVParsers.service // TODO: Inject this to get more generic code (only this Service CSV parser is specific).
        )
      )
}

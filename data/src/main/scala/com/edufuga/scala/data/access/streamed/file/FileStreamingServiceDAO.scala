package com.edufuga.scala.data.access.streamed.file

import cats.effect.IO
import com.edufuga.scala.core.Service
import com.edufuga.scala.core.ServiceTypes.ServiceId
import com.edufuga.scala.data.access.ops.{FileOps, StreamOps}
import com.edufuga.scala.data.access.streamed.StreamingServiceDAO
import com.edufuga.scala.data.parsers.csv.CSVParsers
import fs2.Stream
import fs2.io.file.Files

case class FileStreamingServiceDAO(file: String) extends StreamingServiceDAO {
  override def readAll: Stream[IO, Option[Service]] =
    Files[IO].readAll(FileOps.pathOf(file))
      .through(StreamOps.entitiesParser(CSVParsers.service))

  override def readById(id: ServiceId): Stream[IO, Option[Service]] = readAll.filter(_.exists(_.id.equals(id)))
}

package com.edufuga.scala.data.access.streamed.file

import cats.effect.IO
import com.edufuga.scala.core.Service
import com.edufuga.scala.data.access.ops.{FileOps, StreamOps}
import com.edufuga.scala.data.access.streamed.StreamedServicesDAO
import com.edufuga.scala.data.parsers.csv.CSVParsers
import fs2.Stream
import fs2.io.file.Files

case class FileStreamedServicesDAO(file: String) extends StreamedServicesDAO {
  override def readAll: Stream[IO, Option[Service]] = 
    Files[IO].readAll(FileOps.pathOf(file))
      .through(StreamOps.entitiesParser(CSVParsers.service))
      .evalTap(IO.println)
}

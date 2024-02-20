package com.edufuga.scala.data.access.streamed.file

import cats.effect.IO
import com.edufuga.scala.core.Product
import com.edufuga.scala.data.access.ops.{FileOps, StreamOps}
import com.edufuga.scala.data.access.streamed.StreamingProductDAO
import com.edufuga.scala.data.parsers.csv.CSVParsers
import fs2.Stream
import fs2.io.file.Files

case class FileStreamingProductDAO(file: String) extends StreamingProductDAO {
  override def readAll: Stream[IO, Product] =
    Files[IO].readAll(FileOps.pathOf(file))
      .through(
        StreamOps.entitiesParser(
          CSVParsers.product // TODO: Inject this to get more generic code (only this Product CSV parser is specific).
        )
      )
}

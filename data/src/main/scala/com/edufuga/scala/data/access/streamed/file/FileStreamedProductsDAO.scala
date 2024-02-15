package com.edufuga.scala.data.access.streamed.file

import cats.effect.IO
import com.edufuga.scala.core.Product
import com.edufuga.scala.data.access.ops.{FileOps, StreamOps}
import com.edufuga.scala.data.access.streamed.StreamedProductsDAO
import com.edufuga.scala.data.parsers.csv.CSVParsers
import fs2.Stream
import fs2.io.file.Files

case class FileStreamedProductsDAO(productsFile: String) extends StreamedProductsDAO {
  override def readAll: Stream[IO, Option[Product]] =
    Files[IO].readAll(FileOps.pathOf(productsFile))
      .through(StreamOps.entitiesParser(CSVParsers.product))
      .evalTap(IO.println)
}

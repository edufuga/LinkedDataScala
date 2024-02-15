package com.edufuga.scala.data.access.streamed

import cats.effect.IO
import com.edufuga.scala.data.access.{ReadAll, ReadById}
import fs2.Stream

trait StreamingDAO[Id, O] extends ReadAll[Stream[IO, O]], ReadById[Id, Stream[IO, O]]
package com.edufuga.scala.data.access.streamed

import cats.effect.IO
import com.edufuga.scala.data.access.ReadAll
import fs2.Stream

trait StreamingDAO[O] extends ReadAll[Stream[IO, O]]
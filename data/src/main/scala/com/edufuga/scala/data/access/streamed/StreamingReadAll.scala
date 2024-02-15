package com.edufuga.scala.data.access.streamed

import cats.effect.IO
import com.edufuga.scala.data.access.ReadAll
import fs2.Stream

/**
 * Effectful streaming read interface.
 * @tparam O Type of the outputted data within and through the stream.
 */
trait StreamingReadAll[O] extends ReadAll[Stream[IO, O]]

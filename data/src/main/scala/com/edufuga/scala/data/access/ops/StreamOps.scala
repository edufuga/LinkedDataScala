package com.edufuga.scala.data.access.ops

import fs2.{Pipe, text}

object StreamOps {
  def entitiesParser[F[_], E](toMaybeEntity: String => Option[E]): Pipe[F, Byte, Option[E]] =
    _.through(text.utf8.decode)
      .through(text.lines)
      .drop(1)
      .map(toMaybeEntity)
}

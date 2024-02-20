package com.edufuga.scala.data.access.ops

import fs2.{Pipe, text}

object StreamOps {
  def entitiesPipe[F[_], E](toMaybeEntity: String => Option[E]): Pipe[F, Byte, E] =
    _.through(text.utf8.decode)
      .through(text.lines)
      .filter(_.nonEmpty)
      .drop(1)
      .map(toMaybeEntity)
      .filter(_.nonEmpty) // Discard possible "None"s in the Option type.
      .map(_.get) // Notice that calling "option.get" is normally tabu, but here we KNOW what we're going (filtering).
}

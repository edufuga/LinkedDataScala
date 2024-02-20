package com.edufuga.scala.data.access.streamed

import cats.effect.IO
import com.edufuga.scala.core.Identifiable
import com.edufuga.scala.data.access.entities.ProductDAO
import fs2.Stream

type StreamIOWrapper[+O] = Stream[IO, O]
trait ProductStreamingEffectfulDAO[Id, +F[+_], +O <: Identifiable[Id]] extends ProductDAO[StreamIOWrapper]

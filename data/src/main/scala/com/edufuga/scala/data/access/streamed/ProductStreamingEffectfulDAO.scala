package com.edufuga.scala.data.access.streamed

import cats.effect.IO
import com.edufuga.scala.core.Identifiable
import com.edufuga.scala.data.access.entities.ProductDAO
import fs2.Stream

type StreamingEffectfulWrapper[+O] = Stream[IO, O]

trait ProductStreamingEffectfulDAO[Id, +O <: Identifiable[Id]] extends ProductDAO[StreamingEffectfulWrapper]

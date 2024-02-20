package com.edufuga.scala.data.access.entities

import cats.effect.IO
import fs2.Stream

// Higher-kinded type for adapting from two type parameters (of Stream) to only one type parameter (of our entity DAOs).
type StreamingEffectfulWrapper[+O] = Stream[IO, O]

type ProductStreamingEffectfulDAO = ProductDAO[StreamingEffectfulWrapper]

type ServiceStreamingEffectfulDAO = ServiceDAO[StreamingEffectfulWrapper]

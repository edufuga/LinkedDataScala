package com.edufuga.scala.data.access.entities

import cats.effect.IO
import com.edufuga.scala.core._
import com.edufuga.scala.core.ProductTypes.ProductId
import com.edufuga.scala.core.ServiceTypes.ServiceId
import com.edufuga.scala.data.access.DAO
import fs2.Stream

// Higher-kinded type for adapting from two type parameters (of Stream) to only one type parameter (of our entity DAOs).
type StreamingEffectfulWrapper[+O] = Stream[IO, O]

type ProductDAO[+W[+_]] = DAO[ProductId, W[Product]]
type ProductStreamingEffectfulDAO = ProductDAO[StreamingEffectfulWrapper]

type ServiceDAO[+W[+_]] = DAO[ServiceId, W[Service]]
type ServiceStreamingEffectfulDAO = ServiceDAO[StreamingEffectfulWrapper]

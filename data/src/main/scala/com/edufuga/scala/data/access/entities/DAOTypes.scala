package com.edufuga.scala.data.access.entities

import cats.effect.IO
import com.edufuga.scala.core.*
import com.edufuga.scala.core.ProductTypes.ProductId
import com.edufuga.scala.core.ServiceTypes.ServiceId
import com.edufuga.scala.data.access.{DAO, ReadAll}
import fs2.Stream

type StreamingEffectfulWrapper[S[+_[_], +_], +EF[+_], +O] = S[EF, O]

// Higher-kinded type for adapting from two type parameters (of Stream) to only one type parameter (of our entity DAOs).
type TypeLevelStreamingEffectfulWrapper[+O] = StreamingEffectfulWrapper[Stream, IO, O] // Stream[IO, O]

type ProductDAO[+W[+_]] = DAO[ProductId, W[Product]]
// Streaming DAO.
type ProductTypeLevelStreamingEffectfulDAO = ProductDAO[TypeLevelStreamingEffectfulWrapper]

type ServiceDAO[+W[+_]] = DAO[ServiceId, W[Service]]
// Streaming DAO.
type ServiceTypeLevelStreamingEffectfulDAO = ServiceDAO[TypeLevelStreamingEffectfulWrapper]

// Materialized DAO. ("materialized" = it returns the result without an indirection via an effect or streaming type)
type OrganisationDAO = ReadAll[Option[Organisation]]

// Effectful DAO.
type EffectfulFullOrganisationDAO[+EF[+_]] = ReadAll[EF[Option[FullOrganisation]]]

package com.edufuga.scala.data.access.entities

import cats.effect.IO
import com.edufuga.scala.core.*
import com.edufuga.scala.core.ProductTypes.ProductId
import com.edufuga.scala.core.ServiceTypes.ServiceId
import com.edufuga.scala.data.access.{DAO, ReadAll}
import fs2.Stream

// TODO: Separate this into two cleanly independent parts ("what", "how", "entity-specific how")

/* WHAT (what entities can be retrieved, etc.) */
type ProductDAO[+W[+_]] = DAO[ProductId, W[Product]] // This wrapper can be anything: Option, IO, Stream, etc.
type ServiceDAO[+W[+_]] = DAO[ServiceId, W[Service]] // This wrapper can be anything: Option, IO, Stream, etc.
type FullOrganisationReader[+W[+_]] = ReadAll[W[FullOrganisation]] // This wrapper can be anything: Option, IO, etc.


/* HOW (how the information is transported, independent of the specific entity and independent of the technology) */
type EffectfulStream[S[+_[_], +_], +EF[+_], +O] = S[EF, O] // This is a higher-kinded type alias, meant for streaming.
type EffectfulReader[+EF[+_], +O] = ReadAll[EF[Option[O]]] // The two type parameters are the wrapper and output type.
type MaterializedReader[+O] = ReadAll[Option[O]] // The type parameter is the output type.


/* WHAT + HOW (how the information of each specific entity is retrieved and transported) */
type OrganisationMaterializedDAO = MaterializedReader[Organisation] // = ReadAll[Option[Organisation]]
type FullOrganisationEffectfulDAO[+EF[+_]] = EffectfulReader[EF, FullOrganisation] // = ReadAll[EF[Option[FullOrganisation]]]


/* WHAT + HOW + _technology specific_ (e.g. TypeLevel stack types for Stream, IO, etc.) */

// Higher-kinded type for adapting from two type parameters (of Stream) to only one type parameter (of our entity DAOs).
type TypeLevelEffectfulStream[+O] = EffectfulStream[Stream, IO, O] // = Stream[IO, O]
type TypeLevelEffectType[+O] = IO[O] // This is just a simple type alias (synonym), to make it clear which 'IO' it is.

// Entity and technology specific DAOs. The TypeLevelEffectfulStream higher-kinded type has only one type parameter.
type ProductTypeLevelEffectfulStreamingDAO = ProductDAO[TypeLevelEffectfulStream] // DAO[ProductId, Stream[IO, Product]]
type ServiceTypeLevelEffectfulStreamingDAO = ServiceDAO[TypeLevelEffectfulStream] // DAO[ServiceId, Stream[IO, Service]]
type FullOrganisationTypeLevelEffectfulDAO = FullOrganisationEffectfulDAO[TypeLevelEffectType] // ReadAll[IO[Option[FullOrganisation]]]

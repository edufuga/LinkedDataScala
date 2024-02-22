package com.edufuga.scala.data.access.entities

import cats.effect.IO
import com.edufuga.scala.core.*
import com.edufuga.scala.core.ProductTypes.ProductId
import com.edufuga.scala.core.ServiceTypes.ServiceId
import com.edufuga.scala.data.access.{DAO, ReadAll}
import fs2.Stream

/** Notice the clearly separated parts of "what", "how" and "what + technology-specific how" */

/* WHAT (what entities can be retrieved, etc.) */
type ProductDAO[+W[+_]] = DAO[ProductId, W[Product]] // This wrapper can be anything: Option, IO, Stream, etc.
type ServiceDAO[+W[+_]] = DAO[ServiceId, W[Service]] // This wrapper can be anything: Option, IO, Stream, etc.
type OrganisationReader[+W[+_]] = ReadAll[W[Organisation]] // This wrapper can be anything: Option, IO, etc.
type FullOrganisationReader[+W[+_]] = ReadAll[W[FullOrganisation]] // This wrapper can be anything: Option, IO, etc.


/* HOW (how the information is transported or contained, independent of the specific entity and the technology) */
type EffectfulStream[S[+_[_], +_], +EF[+_], +O] = S[EF, O] // This is a higher-kinded type alias, meant for streaming.
type EffectfulOptional[+EF[+_], +O] = EF[Option[O]] // This is just a simple type alias for effects around optionals.

/* HOW + technology specific (e.g. TypeLevel stack types for Stream, IO, etc.) */
// Higher-kinded type for adapting from two type parameters (of Stream) to only one type parameter (of our entity DAOs).
type TypeLevelEffectfulStream[+O] = EffectfulStream[Stream, IO, O] // = Stream[IO, O]
type TypeLevelEffectfulOptional[+O] = EffectfulOptional[IO, O] // = IO[Option[O]]


/* WHAT + HOW + _technology specific_ (e.g. TypeLevel stack types for Stream, IO, etc.) */
// Entity and technology specific DAOs. The TypeLevelEffectfulStream higher-kinded type has only one type parameter.
type ProductTypeLevelEffectfulStreamingDAO = ProductDAO[TypeLevelEffectfulStream] // DAO[ProductId, Stream[IO, Product]]
type ServiceTypeLevelEffectfulStreamingDAO = ServiceDAO[TypeLevelEffectfulStream] // DAO[ServiceId, Stream[IO, Service]]
type OrganisationMaterializedDAO = OrganisationReader[Option] // = ReadAll[Option[Organisation]]
type FullOrganisationTypeLevelEffectfulDAO = FullOrganisationReader[TypeLevelEffectfulOptional] // ReadAll[IO[Option[FullOrganisation]]]

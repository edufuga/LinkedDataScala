package com.edufuga.scala.operations.entity.implementation

import cats.effect.IO
import com.edufuga.scala.operations.entity.*
import fs2.Stream

/** Notice the clearly separated parts of "what", "how" and "what + technology-specific how" */

object TechnologicalDetailTypes {
  /* HOW (how the information is transported or contained, independent of the specific entity and the technology) */
  private type EffectfulStream[+S[+_[_], +_], EF[+_], +O] = S[EF, O] // This is a higher-kinded type alias, meant for streaming.
  private type EffectfulOptional[EF[+_], +O] = EF[Option[O]] // This is just a simple type alias for effects around optionals.
  private type EffectfulList[EF[+_], +O] = EF[List[O]] // This is just a simple type alias for effects around optionals.

  /* HOW + technology specific (e.g. TypeLevel stack types for Stream, IO, etc.) */
  // Higher-kinded type for adapting from two type parameters (of Stream) to only one type parameter (of our entity DAOs).
  type TypeLevelEffectfulStream[+O] = EffectfulStream[Stream, IO, O] // = Stream[IO, O]
  type TypeLevelEffectfulOptional[+O] = EffectfulOptional[IO, O] // = IO[Option[O]]
  type TypeLevelEffectfulList[+O] = EffectfulList[IO, O] // = IO[List[O]]
}

object EntityOperationImplementationTypes {
  import TechnologicalDetailTypes.*

  /* WHAT + HOW + _technology specific_ (e.g. TypeLevel stack types for Stream, IO, etc.) */
  // Entity and technology specific DAOs. The TypeLevelEffectfulStream higher-kinded type has only one type parameter.
  type ProductTypeLevelEffectfulStreamingDAO = ProductDAO[TypeLevelEffectfulOptional, TypeLevelEffectfulStream] // DAO[ProductId, Product, IO[Option[_], Stream[IO, _]]
  type ServiceTypeLevelEffectfulStreamingDAO = ServiceDAO[TypeLevelEffectfulOptional, TypeLevelEffectfulStream] // DAO[ServiceId, Service, IO[Option[_], Stream[IO, _]]
  type FullServiceTypeLevelEffectfulStreamingDAO = FullServiceReader[TypeLevelEffectfulList] // ReadAll[IO[List[FullService]]]
  type OrganisationMaterializedDAO = OrganisationReader[Option] // = ReadAll[Option[Organisation]]
  type FullOrganisationTypeLevelEffectfulDAO = FullOrganisationReader[TypeLevelEffectfulOptional] // ReadAll[IO[Option[FullOrganisation]]]
}

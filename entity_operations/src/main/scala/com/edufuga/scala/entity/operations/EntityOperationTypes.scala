package com.edufuga.scala.entity.operations

import com.edufuga.scala.entities.*
import com.edufuga.scala.entities.ProductTypes.ProductId
import com.edufuga.scala.entities.ServiceTypes.ServiceId
import com.edufuga.scala.operations.{DAO, ReadAll}

/* WHAT (what entities can be retrieved, etc.) */
type ProductDAO[+W[+_], +S[+_]] = DAO[ProductId, Product, W, S] // This wrapper can be anything: Option, IO, Stream, etc.
type ServiceDAO[+W[+_], +S[+_]] = DAO[ServiceId, Service, W, S] // This wrapper can be anything: Option, IO, Stream, etc.
type OrganisationReader[+W[+_]] = ReadAll[Organisation, W] // This wrapper can be anything: Option, IO, etc.
type FullOrganisationReader[+W[+_]] = ReadAll[FullOrganisation, W] // This wrapper can be anything: Option, IO, etc.

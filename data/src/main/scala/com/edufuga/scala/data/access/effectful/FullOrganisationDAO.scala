package com.edufuga.scala.data.access.effectful

import cats.effect.IO
import cats.implicits.*
import com.edufuga.scala.core.*
import com.edufuga.scala.data.access.ReadAll
import com.edufuga.scala.data.access.entities.{ProductStreamingEffectfulDAO, ServiceStreamingEffectfulDAO}
import com.edufuga.scala.data.access.materialized.MaterializingOrganisationDAO

sealed class FullOrganisationDAO(
  productDAO: ProductStreamingEffectfulDAO,
  serviceDAO: ServiceStreamingEffectfulDAO,
  organisationDAO: MaterializingOrganisationDAO
) extends ReadAll[IO[Option[FullOrganisation]]] {
  override def readAll: IO[Option[FullOrganisation]] = {
    def toEvalFullDepartment(department: Department): IO[FullDepartment] = {
      val productsEval: IO[List[Product]] = productDAO.readByIds(department.productIds).compile.toList
      val servicesEval: IO[List[Service]] = serviceDAO.readByIds(department.serviceIds).compile.toList

      val evalProductsAndServices: IO[(List[Product], List[Service])] = IO.both(productsEval, servicesEval)

      val evalFullDepartment: IO[FullDepartment] = evalProductsAndServices.map { (products, services) =>
        FullDepartment(
          department.id,
          department.name,
          department.manager,
          department.employees,
          products,
          services
        )
      }

      evalFullDepartment
    }

    def toFullOrganisation(organisation: Organisation): IO[FullOrganisation] = {
      val fullDepartmentEvalList: List[IO[FullDepartment]] = organisation.departments.map(toEvalFullDepartment)

      // Convert the List[IO[...]] to IO[List[...]]
      val evalFullDepartments: IO[List[FullDepartment]] = fullDepartmentEvalList.sequence

      val evalFullOrganisation: IO[FullOrganisation] = evalFullDepartments.map(FullOrganisation.apply)

      evalFullOrganisation
    }

    // Notice the '.sequence' to convert from Option[IO[FullOrganisation]] to IO[Option[FullOrganisation]].
    val evalMaybeFullOrganisation: IO[Option[FullOrganisation]] =
      organisationDAO.readAll.map(toFullOrganisation).sequence

    evalMaybeFullOrganisation
  }
}

package com.edufuga.scala.data.access.effectful

import cats.effect.IO
import cats.implicits.*
import com.edufuga.scala.core.*
import com.edufuga.scala.data.access.entities.{EffectfulFullOrganisationDAO, OrganisationDAO, ProductTypeLevelStreamingEffectfulDAO, ServiceTypeLevelStreamingEffectfulDAO}

/**
 * This is an implementation of the DAO for the FullOrganisation entity.
 *
 * The implementation is a composite, i.e. it uses the Product DAO and the Service DAO in order to retrieve those
 * entities via their IDs.
 *
 * Additionally, the DAO is effectful. This means that it uses an effect type such as the IO monad from Cats Effect as a
 * wrapper around the returned (optional) FullOrganisation entity.
 *
 * @param productDAO Product DAO
 * @param serviceDAO Service DAO
 * @param organisationDAO Organisation DAO (with references to Product and Service IDs, which need to be resolved)
 */
sealed class CompositeEffectfulFullOrganisationDAO(
  productDAO: ProductTypeLevelStreamingEffectfulDAO,
  serviceDAO: ServiceTypeLevelStreamingEffectfulDAO,
  organisationDAO: OrganisationDAO
) extends EffectfulFullOrganisationDAO[IO] {
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

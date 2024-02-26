package com.edufuga.scala.operations.entity.implementation.effectful

import cats.effect.IO
import cats.implicits.*
import com.edufuga.scala.entities.*
import com.edufuga.scala.entities.ProductTypes.ProductId
import com.edufuga.scala.entities.ServiceTypes.ServiceId
import com.edufuga.scala.operations.entity.implementation.{FullOrganisationTypeLevelEffectfulDAO, OrganisationMaterializedDAO, ProductTypeLevelEffectfulStreamingDAO, ServiceTypeLevelEffectfulStreamingDAO}

/**
 * This is an implementation of the DAO for the FullOrganisation entity.
 *
 * The implementation is a composite, i.e. it uses the Product DAO and the Service DAO in order to retrieve those
 * entities via their IDs.
 *
 * Additionally, the DAO is effectful. This means that it uses an effect type such as the IO monad from Cats Effect as a
 * wrapper around the returned (optional) FullOrganisation entity.
 *
 * @param productsFromIds Function that returns a list of products from the list of their IDs.
 * @param servicesFromIds Function that returns a list of services from the list of their IDs.
 * @param organisationDAO Organisation DAO (with references to Product and Service IDs, which need to be resolved)
 */
sealed class FullOrganisationTypeLevelEffectfulCombinationDAO(
  productsFromIds: List[ProductId] => IO[List[Product]],
  servicesFromIds: List[ServiceId] => IO[List[Service]],
  organisationDAO: OrganisationMaterializedDAO
) extends FullOrganisationTypeLevelEffectfulDAO {
  override def readAll: IO[Option[FullOrganisation]] = {
    def toEvalFullDepartment(department: Department): IO[FullDepartment] = {
      val productsEval: IO[List[Product]] = productsFromIds(department.productIds)
      val servicesEval: IO[List[Service]] = servicesFromIds(department.serviceIds)

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

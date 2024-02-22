package com.edufuga.scala.streaming

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*
import com.edufuga.scala.core.*
import com.edufuga.scala.data.access.entities.{ProductStreamingEffectfulDAO, ServiceStreamingEffectfulDAO}
import com.edufuga.scala.data.access.materialized.MaterializingOrganisationDAO
import com.edufuga.scala.data.access.materialized.file.FileMaterializingOrganisationDAO
import com.edufuga.scala.data.access.streamed.file.{ProductFileStreamingWithIODAO, ServiceFileStreamingWithIODAO}

import scala.util.{Failure, Success, Try}

class OrganisationStreamer(
  productDAO: ProductStreamingEffectfulDAO,
  serviceDAO: ServiceStreamingEffectfulDAO,
  organisationDAO: MaterializingOrganisationDAO
) {
  def read: IO[Option[FullOrganisation]] = {
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

    for {
      _ <- IO.println("Processing the full organisation. This includes resolving the linked products and services.")
      // Notice the '.sequence' to convert from Option[IO[FullOrganisation]] to IO[Option[FullOrganisation]].
      maybeFullOrganisation <- organisationDAO.readAll.map(toFullOrganisation).sequence
      _ <- IO.println(maybeFullOrganisation)
    } yield maybeFullOrganisation
  }
}

object OrganisationStreamer extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    Try {
      val products: String = args(0)
      val services: String = args(1)
      val organisation: String = args(2)
      (products, services, organisation)
    } match
      case Failure(_) => IO {
        ExitCode.Error
      }
      case Success(p, s, o) =>
        val productsDAO: ProductStreamingEffectfulDAO = ProductFileStreamingWithIODAO(p)
        val servicesDAO: ServiceStreamingEffectfulDAO = ServiceFileStreamingWithIODAO(s)
        val organisationDAO: MaterializingOrganisationDAO = FileMaterializingOrganisationDAO(o)

        val organisationStreamer: OrganisationStreamer =
          new OrganisationStreamer(productsDAO, servicesDAO, organisationDAO)
        for {
          organisation <- organisationStreamer.read
          _ <- IO.println(organisation)
        } yield ExitCode.Success
  }
}

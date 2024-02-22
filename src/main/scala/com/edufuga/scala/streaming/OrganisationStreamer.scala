package com.edufuga.scala.streaming

import cats.effect.unsafe.implicits.global
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
  def stream: IO[ExitCode] = {
    for {
      _ <- IO.println("Processing the organisation file 'orgmap.xml'")
      maybeOrganisation = organisationDAO.readAll
      _ <- IO.println(maybeOrganisation)

      _ <- IO.println("Processing the full organisation...")
      maybeFullOrganisation <- maybeOrganisation.map { organisation =>
        OrganisationStreamer.toFullOrganisation(organisation, productDAO, serviceDAO)
      }.sequence
      _ <- IO.println(maybeFullOrganisation)
    } yield ExitCode.Success
  }
}

object OrganisationStreamer extends IOApp {
  private def toEvalFullDepartment(department: Department,
                           productDAO: ProductStreamingEffectfulDAO,
                           serviceDAO: ServiceStreamingEffectfulDAO): IO[FullDepartment] = {
    println("********************* toEvalFullDepartment *********************")
    println(department)
    val productsEval: IO[List[Product]] = productDAO.readByIds(department.productIds).evalTap(IO.println).compile.toList
    val servicesEval: IO[List[Service]] = serviceDAO.readByIds(department.serviceIds).evalTap(IO.println).compile.toList

    val productsAndServicesEval: IO[(List[Product], List[Service])] = IO.both(productsEval, servicesEval)

    val fullDepartmentEval: IO[FullDepartment] = productsAndServicesEval.map { (products, services) =>
      //println(s"Products: $products")
      //println(s"Services: $services")
      FullDepartment(
        department.id,
        department.name,
        department.manager,
        department.employees,
        products,
        services
      )
    }

    fullDepartmentEval
  }

  private def toFullOrganisation(organisation: Organisation,
                         productDAO: ProductStreamingEffectfulDAO,
                         serviceDAO: ServiceStreamingEffectfulDAO): IO[FullOrganisation] = {
    val fullDepartmentEvalList: List[IO[FullDepartment]] = organisation.departments.map { department =>
      OrganisationStreamer.toEvalFullDepartment(department, productDAO, serviceDAO)
    }

    // Convert the List[IO[...]] to IO[List[...]]
    val evalFullDepartments: IO[List[FullDepartment]] = fullDepartmentEvalList.sequence

    val evalFullOrganisation: IO[FullOrganisation] = evalFullDepartments.map(FullOrganisation.apply)

    evalFullOrganisation
  }

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

        // THIS IS JUST TESTING STUFF....
        val evalMaybeFullOrganisation: IO[Option[FullOrganisation]] = organisationDAO.readAll.map { organisation =>
          OrganisationStreamer.toFullOrganisation(organisation, productsDAO, servicesDAO)
        }.sequence

        val maybeFullOrganisation: Option[FullOrganisation] = evalMaybeFullOrganisation.unsafeRunSync()
        println(s"Maybe full organisation: $maybeFullOrganisation")

        val organisationStreamer: OrganisationStreamer =
          new OrganisationStreamer(productsDAO, servicesDAO, organisationDAO)
        organisationStreamer.stream
  }
}

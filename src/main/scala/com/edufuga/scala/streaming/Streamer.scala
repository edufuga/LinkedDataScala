package com.edufuga.scala.streaming

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.edufuga.scala.core.{Department, FullDepartment, FullOrganisation, Organisation, Product, Service}
import com.edufuga.scala.core.ProductTypes.ProductId
import com.edufuga.scala.core.ServiceTypes.ServiceId
import com.edufuga.scala.data.access.entities.{ProductStreamingEffectfulDAO, ServiceStreamingEffectfulDAO}
import com.edufuga.scala.data.access.materialized.MaterializingOrganisationDAO
import com.edufuga.scala.data.access.materialized.file.FileMaterializingOrganisationDAO
import com.edufuga.scala.data.access.streamed.file.{ProductFileStreamingWithIODAO, ServiceFileStreamingWithIODAO}

import scala.util.{Failure, Success, Try}

class Streamer(
  productDAO: ProductStreamingEffectfulDAO,
  serviceDAO: ServiceStreamingEffectfulDAO,
  organisationDAO: MaterializingOrganisationDAO
) {
  def toEvalFullDepartment(department: Department): IO[FullDepartment] = {
    val productsEval: IO[List[Product]] = productDAO.readByIds(department.productIds).compile.toList
    val servicesEval: IO[List[Service]] = serviceDAO.readByIds(department.serviceIds).compile.toList

    val productsAndServicesEval: IO[(List[Product], List[Service])] = IO.both(productsEval, servicesEval)

    val fullDepartmentEval: IO[FullDepartment] = productsAndServicesEval.map { (products, services) =>
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
  def toFullOrganisation(organisation: Organisation): IO[FullOrganisation] = {
    val fullDepartmentEvalList: List[IO[FullDepartment]] = organisation.departments.map(toEvalFullDepartment)

    // Convert the List[IO[...]] to IO[List[...]]
    val evalFullDepartments: IO[List[FullDepartment]] = fullDepartmentEvalList.sequence

    val evalFullOrganisation: IO[FullOrganisation] = evalFullDepartments.map { fullDepartments =>
      FullOrganisation(fullDepartments)
    }

    evalFullOrganisation
  }

  def stream: IO[ExitCode] = {
    for {
      //_ <- IO.println("Processing 'products.csv', 'services.csv' and 'orgmap.xml'.")

      //_ <- IO.println(s"Processing stream of products.")
      products <- productDAO.readAll.compile.toList
      //_ <- IO.println(products)

      //_ <- IO.println(s"Processing stream of services.")
      services <- serviceDAO.readAll.compile.toList
      //_ <- IO.println(services)

      _ <- IO.println("Processing the organisation file 'orgmap.xml'")
      organisation = organisationDAO.readAll
      _ <- IO.println(organisation)

      _ <- IO.println("Processing the full organisation...")
      fullOrganisation <- organisation.map(toFullOrganisation).sequence
      _ <- IO.println(fullOrganisation)

      // _ <- IO.println(s"Finding a product by ID within the stream of products.")
      bingoProduct <- productDAO.readById(ProductId("X716-6172862")).compile.last
      //_ <- IO.println("Bingo product: " + bingoProduct)

      //_ <- IO.println(s"Finding a service by ID within the stream of services.")
      bingoService <- serviceDAO.readById(ServiceId("Y274-1029755")).compile.last
      // _ <- IO.println("Bingo service: " + bingoService)
    } yield ExitCode.Success
  }
}

object Streamer extends IOApp {
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

        val streamer: Streamer = new Streamer(productsDAO, servicesDAO, organisationDAO)
        streamer.stream
  }
}
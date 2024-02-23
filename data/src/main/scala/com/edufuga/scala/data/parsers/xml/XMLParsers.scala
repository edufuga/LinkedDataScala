package com.edufuga.scala.data.parsers.xml

import com.edufuga.scala.entities.*
import com.edufuga.scala.entities.DepartmentTypes.*
import com.edufuga.scala.entities.EmployeeTypes.*
import com.edufuga.scala.entities.PersonTypes.*
import com.edufuga.scala.entities.ProductTypes.*
import com.edufuga.scala.entities.ServiceTypes.*

import scala.util.{Failure, Success, Try}
import scala.xml.Node

object XMLParsers {
  def serviceId(serviceIdNode: Node): Option[ServiceId] =
    Try {
      (serviceIdNode \ "@id").text
    }.map(ServiceId.apply).toOption

  def productId(productIdNode: Node): Option[ProductId] =
    Try {
      (productIdNode \ "@id").text
    }.map(ProductId.apply).toOption

  def productIds(productsNode: Node): List[ProductId] = {
    val maybeProductIds: Try[List[ProductId]] = Try {
      (productsNode \ "product")
        .flatMap(productId)
        .toList
    }

    maybeProductIds match
      case Failure(_) => List.empty
      case Success(ids) => ids
  }

  def employee(employeeNode: Node): Option[Employee] = {
    Try {
      (
        (employeeNode \ "email").text,
        (employeeNode \ "name").text,
        (employeeNode \ "address").text,
        (employeeNode \ "phone").text,
        (employeeNode \ "productExpert").text
      )
    }.map {
      (e, n, a, p, exp) =>
        (
          Email(e),
          Name(n),
          Option(a).filter(_.nonEmpty).map(Address.apply), // address is optional
          Option(p).filter(_.nonEmpty).map(Phone.apply),   // phone is optional
          exp.split(",").map(_.trim).map(ProductExpert.apply).toList
        )
    }.map(Employee.apply).toOption
  }

  def employees(employeesNode: Node): List[Employee] = {
    val maybeEmployees: Try[List[Employee]] = Try {
      (employeesNode \ "employee")
        .flatMap(employee)
        .toList
    }

    maybeEmployees match
      case Failure(_) => List.empty
      case Success(ids) => ids
  }

  def manager(managerNode: Node): Option[Manager] = {
    Try {
      (
        (managerNode \ "email").text,
        (managerNode \ "name").text,
        (managerNode \ "address").text,
        (managerNode \ "phone").text,
      )
    }.map { (e, n, a, p) =>
      (Email(e), Name(n), Address(a), Phone(p))
    }.map(Manager.apply).toOption
  }

  def department(departmentNode: Node): Option[Department] =
    Try {
      (
        DepartmentId(departmentNode \@ "id"),
        DepartmentName(departmentNode \@ "name"),
        (departmentNode \ "manager")
          .flatMap(manager)
          .head, // Only one "manager", but "\" always returns a NodeSeq
        (departmentNode \ "employees" \ "employee")
          .flatMap(employee)
          .toList,
        (departmentNode \ "products" \ "product")
          .flatMap(productId)
          .toList,
        (departmentNode \ "services" \ "service")
          .flatMap(serviceId)
          .toList,
      )
    }.map { (departmentId, departmentName, manager, employees, productIds, serviceIds) =>
      Department.apply(departmentId, departmentName, manager, employees, productIds, serviceIds)
    }.toOption

  def organisation(organisationNode: Node): Option[Organisation] = {
    Try {
      (organisationNode \ "dept")
        .flatMap(department)
        .toList
    }.map(Organisation.apply).toOption
  }
}

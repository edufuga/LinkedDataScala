package com.edufuga.scala.ogm

import scala.jdk.CollectionConverters.*
import com.edufuga.scala.entities as ent
import com.edufuga.scala.entities.DepartmentTypes.*
import com.edufuga.scala.entities.EmployeeTypes.*
import com.edufuga.scala.entities.PersonTypes.*
import com.edufuga.scala.entities.ProductTypes.*
import com.edufuga.scala.entities.ServiceTypes.*
import productdata.rdf.model as ont
import productdata.rdf.model.Employee

import scala.util.{Failure, Success, Try}

object ObjectGraphMappings {
  private val NAMESPACE: String = "https://github.com/edufuga/LinkedDataScala/"

  private def stringify(text: String) = text.replaceAll(" ", "_")

  // SMELL: Using an obviously stateful counter for setting the "instance IDs" of the graph objects.
  //  A better solution would be to create the instance ID from a certain property of the objects themselves, but well.
  private class Identity(val countableThing: String) {
    private var counter: Integer = 1 // I hate my life.
    def id(): String = {
      val id = s"${countableThing}_${counter}"
      counter = counter + 1 // I hate my life.
      id
    }
  }

  object OrganisationMappings extends ObjectGraphMapping[ent.FullOrganisation, ont.IOrganisation] {
    private val IDENTITY = Identity("organisation")

    override def objectToGraph(entity: ent.FullOrganisation): ont.IOrganisation = {
      val graph = ont.Organisation(NAMESPACE, IDENTITY.id())
      entity.departments.foreach { department =>
        graph.addDepartments(
          DepartmentMappings.objectToGraph(department)
        )
      }
      graph
    }

    override def graphToObject(graph: ont.IOrganisation): ent.FullOrganisation =
      ent.FullOrganisation(
        departments = graph.getDepartments.asScala.map(DepartmentMappings.graphToObject).toList
      )
  }

  object DepartmentMappings extends ObjectGraphMapping[ent.FullDepartment, ont.IDepartment] {
    override def objectToGraph(entity: ent.FullDepartment): ont.IDepartment = {
      val graph = ont.Department(NAMESPACE, s"department_${entity.id}")

      graph.setId(s"${entity.id}")
      graph.setName(s"${entity.name}")
      graph.addManager(ManagerMappings.objectToGraph(entity.manager))

      entity.employees.foreach { employee =>
        graph.addEmployees(
          EmployeeMappings.objectToGraph(employee)
        )
      }

      entity.products.foreach { product =>
        graph.addProducts(
          ProductMappings.objectToGraph(product)
        )
      }

      entity.services.foreach { service =>
        graph.addServices(
          ServiceMappings.objectToGraph(service)
        )
      }

      graph
    }
    override def graphToObject(graph: ont.IDepartment): ent.FullDepartment =
      ent.FullDepartment(
        id = DepartmentId(graph.getId),
        name = DepartmentName(graph.getName),
        manager = ManagerMappings.graphToObject(graph.getManager.asScala.head),
        employees = graph.getEmployees.asScala.map(EmployeeMappings.graphToObject).toList,
        products = graph.getProducts.asScala.map(ProductMappings.graphToObject).toList,
        services = graph.getServices.asScala.map(ServiceMappings.graphToObject).toList
      )
  }

  object ManagerMappings extends ObjectGraphMapping[ent.Manager, ont.IManager] {
    override def objectToGraph(entity: ent.Manager): ont.IManager = {
      val graph = ont.Manager(NAMESPACE, stringify(s"manager_${entity.name}"))
      graph.setEmail(s"${entity.email}")
      graph.setName(s"${entity.name}")
      graph.setAddress(s"${entity.address}")
      graph.setPhone(s"${entity.phone}")
      graph
    }

    override def graphToObject(graph: ont.IManager): ent.Manager =
      ent.Manager(
        email = Email(graph.getEmail),
        name = Name(graph.getName),
        address = Address(graph.getAddress),
        phone = Phone(graph.getPhone)
      )
  }
  
  object EmployeeMappings extends ObjectGraphMapping[ent.Employee, ont.IEmployee] {
    override def objectToGraph(entity: ent.Employee): ont.IEmployee = {
      val graph = ont.Employee(NAMESPACE, stringify(s"employee_${entity.name}"))
      graph.setEmail(s"${entity.email}")
      graph.setName(s"${entity.name}")
      entity.address.foreach(address => graph.setAddress(s"$address"))
      entity.phone.foreach(phone => graph.setPhone(s"$phone"))
      graph.setProductExpertFor(entity.productExpert.mkString(", ")) // Dom dom dom
      graph
    }

    override def graphToObject(graph: ont.IEmployee): ent.Employee =
      ent.Employee(
        email = Email(graph.getEmail),
        name = Name(graph.getName),
        address = {
          Try {
            graph.getAddress // XXX: This may crash with a NoSuchElementException!
          } match
            case Failure(_) => None
            case Success(value) => Some(Address(value))
        },
        phone = {
          Try {
            graph.getPhone // XXX: This may crash with a NoSuchElementException!
          } match
            case Failure(_) => None
            case Success(value) => Some(Phone(value))
        },
        productExpert = graph.getProductExpertFor.split(", ").map(ProductExpert.apply).toList
      )
  }

  object ProductMappings extends ObjectGraphMapping[ent.Product, ont.IProduct] {
    override def objectToGraph(entity: ent.Product): ont.IProduct = {
      val graph = ont.Product(NAMESPACE, stringify(s"product_${entity.id}"))
      graph.setId(s"${entity.id}")
      graph.setName(s"${entity.productName}")
      s"${entity.height}".toIntOption.foreach(height => graph.setHeight(java.math.BigDecimal(height))) // Ouch
      s"${entity.width}".toIntOption.foreach(width => graph.setWidth(java.math.BigDecimal(width)))     // Ouch
      s"${entity.depth}".toIntOption.foreach(depth => graph.setDepth(java.math.BigDecimal(depth)))     // Ouch
      s"${entity.weight}".toIntOption.foreach(weight => graph.setWeight(java.math.BigDecimal(weight))) // Ouch
      graph.setProductManager(s"${entity.productManager}")
      graph.addPrice(MoneyMappings.objectToGraph(entity.price))
      graph
    }
    override def graphToObject(graph: ont.IProduct): ent.Product = {
      ent.Product(
        id = ProductId(graph.getId),
        productName = ProductName(graph.getName),
        height = Height(graph.getHeight.toInt),
        width = Width(graph.getWidth.toInt),
        depth = Depth(graph.getDepth.toInt),
        weight = Weight(graph.getWeight.toInt),
        productManager = ProductManager(graph.getProductManager),
        price = MoneyMappings.graphToObject(graph.getPrice.asScala.head)
      )
    }
  }

  object ServiceMappings extends ObjectGraphMapping[ent.FullService, ont.IService] {
    override def objectToGraph(entity: ent.FullService): ont.IService = {
      val graph = ont.Service(NAMESPACE, stringify(s"service_${entity.id}"))
      graph.setId(s"${entity.id}")
      graph.setName(s"${entity.serviceName}")

      entity.products.foreach { product =>
        graph.addProducts(
          ProductMappings.objectToGraph(product)
        )
      }

      graph.setProductManager(s"${entity.productManager}")
      graph.addPrice(MoneyMappings.objectToGraph(entity.price))
      graph
    }

    override def graphToObject(graph: ont.IService): ent.FullService =
      ent.FullService(
        id = ServiceId(graph.getId),
        serviceName = ServiceName(graph.getName),
        products = graph.getProducts.asScala.toList.map(ProductMappings.graphToObject),
        productManager = ProductManager(graph.getProductManager),
        price = MoneyMappings.graphToObject(graph.getPrice.asScala.head)
      )
  }

  object MoneyMappings extends ObjectGraphMapping[ent.Money, ont.IMoney] {
    override def objectToGraph(entity: ent.Money): ont.IMoney = {
      // This is (conceptually) a "value type", so we don't need a counter.
      // The ID is the VALUE, ex. "100_EUR".
      val graph = ont.Money(NAMESPACE, s"money_${entity.value}_${entity.currency}".replaceAll("\\.", "_"))
      graph.setMonetaryValue(entity.value)
      graph.setCurrency(s"${entity.currency}")
      graph
    }

    override def graphToObject(graph: ont.IMoney): ent.Money =
      ent.Money.mkMoney(graph.getMonetaryValue, graph.getCurrency).orNull
  }
}

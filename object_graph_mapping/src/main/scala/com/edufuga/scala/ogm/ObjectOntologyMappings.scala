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

object ObjectOntologyMappings {
  private val NAMESPACE: String = "https://github.com/edufuga/LinkedDataScala/"

  private def stringify(text: String) = text.replaceAll(" ", "_")

  // SMELL: Using an obviously stateful counter for setting the "instance IDs" of the ontology objects.
  //  A better solution would be to create the instance ID from a certain property of the objects themselves, but well.
  private class Identity(val countableThing: String) {
    private var counter: Integer = 1 // I hate my life.
    def id(): String = {
      val id = s"${countableThing}_${counter}"
      counter = counter + 1 // I hate my life.
      id
    }
  }

  object OrganisationMappings extends ObjectOntologyMapping[ent.FullOrganisation, ont.IOrganisation] {
    private val IDENTITY = Identity("organisation")

    override def objectToOntology(entity: ent.FullOrganisation): ont.IOrganisation = {
      val ontology = ont.Organisation(NAMESPACE, IDENTITY.id())
      entity.departments.foreach { department =>
        ontology.addDepartments(
          DepartmentMappings.objectToOntology(department)
        )
      }
      ontology
    }

    override def ontologyToObject(ontology: ont.IOrganisation): ent.FullOrganisation =
      ent.FullOrganisation(
        departments = ontology.getDepartments.asScala.map(DepartmentMappings.ontologyToObject).toList
      )
  }

  object DepartmentMappings extends ObjectOntologyMapping[ent.FullDepartment, ont.IDepartment] {
    override def objectToOntology(entity: ent.FullDepartment): ont.IDepartment = {
      val ontology = ont.Department(NAMESPACE, s"department_${entity.id}")

      ontology.setId(s"${entity.id}")
      ontology.setName(s"${entity.name}")
      ontology.addManager(ManagerMappings.objectToOntology(entity.manager))

      entity.employees.foreach { employee =>
        ontology.addEmployees(
          EmployeeMappings.objectToOntology(employee)
        )
      }

      entity.products.foreach { product =>
        ontology.addProducts(
          ProductMappings.objectToOntology(product)
        )
      }

      entity.services.foreach { service =>
        ontology.addServices(
          ServiceMappings.objectToOntology(service)
        )
      }

      ontology
    }
    override def ontologyToObject(ontology: ont.IDepartment): ent.FullDepartment =
      ent.FullDepartment(
        id = DepartmentId(ontology.getId),
        name = DepartmentName(ontology.getName),
        manager = ManagerMappings.ontologyToObject(ontology.getManager.asScala.head),
        employees = ontology.getEmployees.asScala.map(EmployeeMappings.ontologyToObject).toList,
        products = ontology.getProducts.asScala.map(ProductMappings.ontologyToObject).toList,
        services = ontology.getServices.asScala.map(ServiceMappings.ontologyToObject).toList
      )
  }

  object ManagerMappings extends ObjectOntologyMapping[ent.Manager, ont.IManager] {
    override def objectToOntology(entity: ent.Manager): ont.IManager = {
      val ontology = ont.Manager(NAMESPACE, stringify(s"manager_${entity.name}"))
      ontology.setEmail(s"${entity.email}")
      ontology.setName(s"${entity.name}")
      ontology.setAddress(s"${entity.address}")
      ontology.setPhone(s"${entity.phone}")
      ontology
    }

    override def ontologyToObject(ontology: ont.IManager): ent.Manager =
      ent.Manager(
        email = Email(ontology.getEmail),
        name = Name(ontology.getName),
        address = Address(ontology.getAddress),
        phone = Phone(ontology.getPhone)
      )
  }
  
  object EmployeeMappings extends ObjectOntologyMapping[ent.Employee, ont.IEmployee] {
    override def objectToOntology(entity: ent.Employee): ont.IEmployee = {
      val ontology = ont.Employee(NAMESPACE, stringify(s"employee_${entity.name}"))
      ontology.setEmail(s"${entity.email}")
      ontology.setName(s"${entity.name}")
      entity.address.foreach(address => ontology.setAddress(s"$address"))
      entity.phone.foreach(phone => ontology.setPhone(s"$phone"))
      ontology.setProductExpertFor(entity.productExpert.mkString(", ")) // Dom dom dom
      ontology
    }

    override def ontologyToObject(ontology: ont.IEmployee): ent.Employee =
      ent.Employee(
        email = Email(ontology.getEmail),
        name = Name(ontology.getName),
        address = Some(Address(ontology.getAddress)),
        phone = Some(Phone(ontology.getPhone)),
        productExpert = ontology.getProductExpertFor.split(", ").map(ProductExpert.apply).toList
      )
  }

  object ProductMappings extends ObjectOntologyMapping[ent.Product, ont.IProduct] {
    override def objectToOntology(entity: ent.Product): ont.IProduct = {
      val ontology = ont.Product(NAMESPACE, stringify(s"product_${entity.id}"))
      ontology.setId(s"${entity.id}")
      ontology.setName(s"${entity.productName}")
      s"${entity.height}".toIntOption.foreach(height => ontology.setHeight(java.math.BigDecimal(height))) // Ouch
      s"${entity.width}".toIntOption.foreach(width => ontology.setWidth(java.math.BigDecimal(width)))     // Ouch
      s"${entity.depth}".toIntOption.foreach(depth => ontology.setDepth(java.math.BigDecimal(depth)))     // Ouch
      s"${entity.weight}".toIntOption.foreach(weight => ontology.setWeight(java.math.BigDecimal(weight))) // Ouch
      ontology.setProductManager(s"${entity.productManager}")
      ontology.addPrice(MoneyMappings.objectToOntology(entity.price))
      ontology
    }
    override def ontologyToObject(ontology: ont.IProduct): ent.Product = {
      ent.Product(
        id = ProductId(ontology.getId),
        productName = ProductName(ontology.getName),
        height = Height(ontology.getHeight.toInt),
        width = Width(ontology.getWidth.toInt),
        depth = Depth(ontology.getDepth.toInt),
        weight = Weight(ontology.getWeight.toInt),
        productManager = ProductManager(ontology.getProductManager),
        price = MoneyMappings.ontologyToObject(ontology.getPrice.asScala.head)
      )
    }
  }

  object ServiceMappings extends ObjectOntologyMapping[ent.FullService, ont.IService] {
    override def objectToOntology(entity: ent.FullService): ont.IService = {
      val ontology = ont.Service(NAMESPACE, stringify(s"service_${entity.id}"))
      ontology.setId(s"${entity.id}")
      ontology.setName(s"${entity.serviceName}")

      entity.products.foreach { product =>
        ontology.addProducts(
          ProductMappings.objectToOntology(product)
        )
      }

      ontology.setProductManager(s"${entity.productManager}")
      ontology.addPrice(MoneyMappings.objectToOntology(entity.price))
      ontology
    }

    override def ontologyToObject(ontology: ont.IService): ent.FullService =
      ent.FullService(
        id = ServiceId(ontology.getId),
        serviceName = ServiceName(ontology.getName),
        products = ontology.getProducts.asScala.toList.map(ProductMappings.ontologyToObject),
        productManager = ProductManager(ontology.getProductManager),
        price = MoneyMappings.ontologyToObject(ontology.getPrice.asScala.head)
      )
  }

  object MoneyMappings extends ObjectOntologyMapping[ent.Money, ont.IMoney] {
    override def objectToOntology(entity: ent.Money): ont.IMoney = {
      // This is (conceptually) a "value type", so we don't need a counter.
      // The ID is the VALUE, ex. "100_EUR".
      val ontology = ont.Money(NAMESPACE, s"money_${entity.value}_${entity.currency}".replaceAll("\\.", "_"))
      ontology.setMonetaryValue(entity.value)
      ontology.setCurrency(s"${entity.currency}")
      ontology
    }

    override def ontologyToObject(ontology: ont.IMoney): ent.Money =
      ent.Money.mkMoney(ontology.getMonetaryValue, ontology.getCurrency).orNull
  }
}

package com.edufuga.scala.ogm.example

import com.edufuga.scala.entities.*
import com.edufuga.scala.entities.DepartmentTypes.*
import com.edufuga.scala.entities.EmployeeTypes.*
import com.edufuga.scala.entities.PersonTypes.*
import com.edufuga.scala.entities.ProductTypes.*
import com.edufuga.scala.entities.ServiceTypes.*

import java.util.Currency

object ObjectConstructionExample extends App {
  def organisation: FullOrganisation = FullOrganisation(
    departments = List(
      FullDepartment(
        id = DepartmentId("73191"),
        name = DepartmentName("Engineering"),
        manager = Manager(
          email = Email("Thomas.Mueller@company.org"),
          name = Name("Thomas Mueller"),
          address = Address("Karl-Liebknecht-Straße 885, 82003 Tettnang"),
          phone = Phone("+49-8200-38218301")
        ),
        employees = List(
          Employee(
            email = Email("Corinna.Ludwig@company.org"),
            name = Name("Corinna Ludwig"),
            address = Some(Address("Ringstraße 276")),
            phone = Some(Phone("+49-1743-24836762")),
            productExpert = List(
              ProductExpert("Memristor"),
              ProductExpert("Gauge"),
              ProductExpert("Encoder")
            )
          )
        ),
        products = List(
          Product(
            id = ProductId("I241-8776317"),
            productName = ProductName("Strain Compensator"), // I always find this name funny.
            height = Height(12),
            width = Width(68),
            depth = Depth(15),
            weight = Weight(8),
            productManager = ProductManager("Baldwin.Dirksen@company.org"), // Good old Baldwin Dirksen.
            price = Money(
              value = 0.5,
              currency = Currency.getInstance("EUR")) // European Bucks
            )
          ),
        services = List(
          FullService(
            id = ServiceId("I241-8776317"),
            serviceName = ServiceName("Component Confabulation"),
            products = List(
              Product(
                id = ProductId("I241-8776317"),
                productName = ProductName("Strain Compensator"), // I always find this name funny.
                height = Height(12),
                width = Width(68),
                depth = Depth(15),
                weight = Weight(8),
                productManager = ProductManager("Baldwin.Dirksen@company.org"), // Good old Baldwin Dirksen.
                price = Money(
                  value = 0.5,
                  currency = Currency.getInstance("EUR")) // European Bucks
              )
            ),
            productManager = ProductManager("Baldwin.Dirksen@company.org"), // Good old Baldwin Dirksen.,
            price = Money(
              value = 12345,
              currency = Currency.getInstance("EUR")) // European Bucks
          )
        )
      )
    )
  )

  println(s"[ObjectConstructionExample] Organisation object: $organisation")
}

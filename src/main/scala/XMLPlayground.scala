package com.edufuga.scala.streaming

import ServiceTypes.ServiceId
import ProductTypes.ProductId
import EmployeeTypes._

import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, Group, Node, NodeSeq, SpecialNode}

object XMLPlayground {
  def parseServiceId(serviceIdNode: Node): Option[ServiceId] =
    Try {
      (serviceIdNode \ "@id").text
    }.map(ServiceId.apply).toOption

  def parseProductId(productIdNode: Node): Option[ProductId] =
    Try {
      (productIdNode \ "@id").text
    }.map(ProductId.apply).toOption

  def parseProductIds(productsNode: Node): List[ProductId] = {
    val maybeProductIds: Try[List[ProductId]] = Try {
      (productsNode \ "product")
        .flatMap(parseProductId)
        .toList
    }

    maybeProductIds match
      case Failure(_) => List.empty
      case Success(ids) => ids
  }

  def parseEmployee(employeeNode: Node): Option[Employee] = {
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
          Email.apply(e),
          Name.apply(n),
          Address.apply(a),
          Phone.apply(p),
          exp.split(",").map(_.trim).map(ProductExpert.apply).toList
        )
    }.map(Employee.apply).toOption
  }

  def parseEmployees(employeesNode: Node): List[Employee] = {
    val maybeEmployees: Try[List[Employee]] = Try {
      (employeesNode \ "employee")
        .flatMap(parseEmployee)
        .toList
    }

    maybeEmployees match
      case Failure(_) => List.empty
      case Success(ids) => ids
  }

  def main(args: Array[String]): Unit = {
    import scala.xml._

    val stuff: Elem = <a>stuff</a>
    println(stuff)

    val moreStuff: Elem = <a>
      {"hello" + ", world"}
    </a>
    println(moreStuff)

    val manager: Elem =
      <manager>
        <email>Thomas.Mueller@company.org</email>
        <name>Thomas Mueller</name>
        <address>Karl-Liebknecht-Straße 885, 82003 Tettnang</address>
        <phone>+49-8200-38218301</phone>
      </manager>

    println(manager)
    println(manager \ "email")
    println((manager \ "email").text)

    val employees: Elem =
      <employees>
        <employee>
          <email>Corinna.Ludwig@company.org</email>
          <name>Corinna Ludwig</name>
          <address>Ringstraße 276</address>
          <phone>+49-1743-24836762</phone>
          <productExpert>Memristor, Gauge, Encoder</productExpert>
        </employee>
        <employee>
          <email>Karen.Brant@company.org</email>
          <name>Karen Brant</name>
          <address>Friedrichstraße 664, 30805 Willich</address>
          <phone>(00530) 5040048</phone>
          <productExpert>Inductor</productExpert>
        </employee>
      </employees>

    val twoEmployees: NodeSeq = employees \\ "employee"
    twoEmployees.foreach(println)

    val emails = employees \ "employee" \ "email"
    println(emails)

    val dep: Elem = <dept id="73191" name="Engineering"/>

    val depId = dep \ "@id"
    println(depId)
    println(dep \ "@name")

    val products: Elem =
      <products>
        <product id="Z249-1364492"/>
        <product id="O184-6903943"/>
        <product id="V404-9975399"/>
        <product id="F344-7012314"/>
        <product id="N463-8050264"/>
        <product id="M605-5951566"/>
        <product id="N733-1946687"/>
      </products>

    println("Parsing a list of products, by hand")
    val productList: NodeSeq = products \ "product"
    productList.foreach(println)
    productList.map(product => product \@ "id").foreach(println)

    println("Parsing a service ID")
    val serviceNode: Elem = <service id="I241-8776317" />
    val maybeServiceId: Option[ServiceId] = parseServiceId(serviceNode)
    println(maybeServiceId)

    println("Parsing a single product ID")
    val productNode: Elem = <product id="Z249-1364492" />
    val maybeProductId: Option[ProductId] = parseProductId(productNode)
    println(maybeProductId)

    println("Parsing product IDs")
    val productIds = parseProductIds(products)
    println(productIds)

    val employee: Elem =
      <employee>
        <email>Anamchara.Foerstner@company.org</email>
        <name>Anamchara Foerstner</name>
        <address>Jüdenstraße 79, 61467 Leonberg</address>
        <phone>+49-360-5655698</phone>
        <productExpert>Inductor, Transistor, Sensor, Gauge, Resonator</productExpert>
      </employee>

    println("Parsing single employee (email, name, address, phone, productExpert)")
    val maybeEmployee = parseEmployee(employee)
    for employee <- maybeEmployee yield println(employee)

    println("Parsing list of employees")
    val maybeEmployees: List[Employee] = parseEmployees(employees)
    println(maybeEmployees)

    // TODO: Parse manager (email, name, address, phone) (common with employee fields, except the productExpert).

    // TODO: Parse department (id and name)
  }
}

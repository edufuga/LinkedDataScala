package com.edufuga.scala.streaming

import ServiceTypes.ServiceId
import ProductTypes.ProductId

import scala.util.Try
import scala.xml.{Elem, Group, SpecialNode}

object XMLPlayground {
  def parseServiceReference(serviceReferenceNode: scala.xml.Node): Option[ServiceId] = {
    Try {
      (serviceReferenceNode \ "@id").text
    }.map(ServiceId.apply).toOption
  }

  def parseProductReference(productReferenceNode: scala.xml.Node): Option[ProductId] = {
    Try {
      (productReferenceNode \ "@id").text
    }.map(ProductId.apply).toOption
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

    val productList: NodeSeq = products \ "product"
    productList.foreach(println)
    productList.map(product => product \@ "id").foreach(println)

    val serviceNode: Elem = <service id="I241-8776317" />
    val maybeService: Option[ServiceId] = parseServiceReference(serviceNode)
    println(maybeService)

    val productNode: Elem = <product id="Z249-1364492" />
    val maybeProduct: Option[ProductId] = parseProductReference(productNode)
    println(maybeProduct)
  }
}
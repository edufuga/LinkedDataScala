package com.edufuga.scala.core

import com.edufuga.scala.core.EmployeeTypes.ProductExpert
import com.edufuga.scala.core.PersonTypes.{Address, Email, Name, Phone}

case class Employee(
  email: Email,
  name: Name,
  address: Option[Address],
  phone: Option[Phone],
  productExpert: List[ProductExpert]
)

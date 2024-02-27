package com.edufuga.scala.entities

import com.edufuga.scala.entities.PersonTypes.{Address, Email, Name, Phone}

case class Manager(
  email: Email,
  name: Name,
  address: Address,
  phone: Phone
)

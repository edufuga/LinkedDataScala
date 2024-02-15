package com.edufuga.scala.core

import com.edufuga.scala.core.PersonTypes.{Address, Email, Name, Phone}

case class Manager(
                    email: Email,
                    name: Name,
                    address: Address,
                    phone: Phone
                  )

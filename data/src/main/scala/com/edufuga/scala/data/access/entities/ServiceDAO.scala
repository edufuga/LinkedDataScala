package com.edufuga.scala.data.access.entities

import com.edufuga.scala.core.Service
import com.edufuga.scala.core.ServiceTypes.ServiceId
import com.edufuga.scala.data.access.DAO

/**
 * Service DAO, generic in a wrapper type with only one parameter.
 *
 */
trait ServiceDAO[+W[+_]] extends DAO[ServiceId, W[Service]]

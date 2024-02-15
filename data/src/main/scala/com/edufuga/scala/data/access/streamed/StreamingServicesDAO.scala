package com.edufuga.scala.data.access.streamed

import cats.effect.IO
import com.edufuga.scala.core.Service
import com.edufuga.scala.core.ServiceTypes.ServiceId

/**
 * Data access to Services.
 * 
 */
trait StreamingServicesDAO extends StreamingDAO[ServiceId, IO, Option[Service]]

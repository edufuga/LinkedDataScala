package com.edufuga.scala.data.access.streamed

import com.edufuga.scala.core.Service
import com.edufuga.scala.core.ServiceTypes.ServiceId

/**
 * Data access to Services.
 * 
 */
trait ServiceStreamingWithIODAO extends StreamingWithIODAO[ServiceId, Service]

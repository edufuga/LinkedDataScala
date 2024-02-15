package com.edufuga.scala.data.access.streamed

import com.edufuga.scala.core.Service

/**
 * Data access to Services.
 * 
 */
trait StreamingServicesDAO extends StreamingDAO[Option[Service]]

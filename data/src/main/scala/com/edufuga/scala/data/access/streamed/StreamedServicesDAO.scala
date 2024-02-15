package com.edufuga.scala.data.access.streamed

import com.edufuga.scala.core.Service

/**
 * Data access to Services.
 * 
 */
trait StreamedServicesDAO extends StreamingReadAll[Option[Service]]

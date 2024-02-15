package com.edufuga.scala.data.access.streamed

import cats.effect.IO
import com.edufuga.scala.core.Service
import com.edufuga.scala.data.access.ReadAll
import fs2.Stream

/**
 * Data access to Services.
 * 
 */
trait StreamedServicesDAO extends ReadAll[Stream[IO, Option[Service]]]

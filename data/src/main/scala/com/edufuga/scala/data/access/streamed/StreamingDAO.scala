package com.edufuga.scala.data.access.streamed

import com.edufuga.scala.data.access.ChannelingEffectfulDAO
import fs2.Stream

trait StreamingDAO[Id, +F[_], +O] extends ChannelingEffectfulDAO[Id, Stream, F, O]

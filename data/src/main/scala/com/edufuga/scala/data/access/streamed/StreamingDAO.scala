package com.edufuga.scala.data.access.streamed

import com.edufuga.scala.data.access.EffectfulDAO
import fs2.Stream

trait StreamingDAO[Id, +F[_], +O] extends EffectfulDAO[Id, F, O, Stream]

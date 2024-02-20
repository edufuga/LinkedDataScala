package com.edufuga.scala.data.access.streamed

trait StreamingOptionalDAO[Id, +O] extends StreamingDAO[Id, Option[O]]

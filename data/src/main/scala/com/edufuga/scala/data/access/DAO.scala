package com.edufuga.scala.data.access

trait DAO[Id, +O] extends ReadAll[O], ReadById[Id, O]

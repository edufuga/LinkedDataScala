package com.edufuga.scala.data.access

trait DAO[Id, +Object] extends ReadAll[Object], ReadById[Id, Object]

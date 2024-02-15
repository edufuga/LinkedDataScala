package com.edufuga.scala.data.access

trait EffectfulDAO[Id, +F[_], +O, Channel[+_[_], +_]] extends DAO[Id, Channel[F, O]]

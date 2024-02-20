package com.edufuga.scala.core

/**
 * This trait represents the ID of an entity.
 * 
 * @tparam Id Type of the ID of the entity.
 */
trait Identifiable[Id] {
  def id: Id
}

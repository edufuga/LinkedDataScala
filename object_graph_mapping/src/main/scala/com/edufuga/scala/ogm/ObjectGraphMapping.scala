package com.edufuga.scala.ogm

trait ObjectGraphMapping[Entity, Graph] {
  def objectToGraph(entity: Entity): Graph
  def graphToObject(graph: Graph): Entity
}

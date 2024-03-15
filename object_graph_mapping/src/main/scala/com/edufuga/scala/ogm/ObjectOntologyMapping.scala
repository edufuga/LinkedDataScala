package com.edufuga.scala.ogm

trait ObjectOntologyMapping[Entity, Ontology] {
  def objectToOntology(entity: Entity): Ontology
  // def ontologyToObject(ontology: Ontology): Entity
}

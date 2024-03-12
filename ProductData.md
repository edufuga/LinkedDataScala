# Ontology library for IoT-related Product Data

The Java code in the subproject [productdata-RDF4J-java](productdata-RDF4J-java) was **generated** with [OLGA](https://ecostruxure.github.io/OLGA/),
the Ontology Library Generator.

This automatically generated code folder is a companion to the rest of the
[Linked Data project](https://github.com/edufuga/LinkedDataScala/).
This automatically generated code is a _representation_ of the [ontology file](linked_data_ontology_rdf.owl)
which is further [described here](Ontology.md).

For more background information on the **model driven approach** and **OLGA**, please read the article 
[A Model Driven Approach Accelerating Ontology-based IoT Applications Development](
https://ceur-ws.org/Vol-2063/sisiot-paper4.pdf
).

## Build as a standalone Maven subproject

The folder `productdata-RDF4J-java` is embedded in the **sbt project** with the rest of the Scala code, but it can
_also_ be compiled standalone. To do that, execute the following command within the folder `productdata-RDF4J-java`:

```
mvn clean install
```

Notice that this won't work from the root folder, for the simple reason that it's a sbt-based project with Scala code.

package com.edufuga.scala.streaming

import cats.effect.{ExitCode, IO}
import com.edufuga.scala.entities.FullOrganisation
import com.edufuga.scala.entities.ProductTypes.ProductId
import com.edufuga.scala.entities.ServiceTypes.ServiceId
import com.edufuga.scala.ogm.ObjectGraphMappings
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.*
import com.edufuga.scala.operations.entity.implementation.effectful.graph.FullOrganisationTypeLevelEffectfulGraphDAO
import org.eclipse.rdf4j.rio.{RDFFormat, Rio}
import productdata.global.util.GLOBAL
import productdata.rdf.model.IOrganisation

import java.io.FileOutputStream

// Notice that this Streamer is still quite implementation (TypeLevel) specific.
// Both the interface (parameters) and the implementation are full of IO and Streams and stuff.
//
// The Streamer should NOT CREATE stuff by itself IN THIS HARD-CODED WAY!
// WE DON'T WANT TO KNOW THE SUBTYPES SUCH AS FullOrganisationTypeLevelEffectfulCombinationDAO
// This is similar to the _previous_ Streamer, but it should be the "StreamerApp" _class_.
class Streamer(
  productDAO: ProductTypeLevelEffectfulStreamingDAO,
  serviceDAO: ServiceTypeLevelEffectfulStreamingDAO,
  organisationDAO: OrganisationMaterializedDAO,
  fullOrganisationDAO: FullOrganisationTypeLevelEffectfulDAO,
  organisationFile: String
) {
  def stream: IO[ExitCode] = {
    for {
      _ <- IO.println("Processing 'products.csv', 'services.csv' and 'orgmap.xml'.")

      _ <- IO.println(s"Processing stream of products.")
      products <- productDAO.readAll.compile.toList
      _ <- IO.println(products)

      _ <- IO.println(s"Processing stream of services.")
      services <- serviceDAO.readAll.compile.toList
      _ <- IO.println(services)

      _ <- IO.println("Processing the organisation file 'orgmap.xml'")
      maybeOrganisation = organisationDAO.readAll
      _ <- IO.println(maybeOrganisation)

      _ <- IO.println(s"Finding a product by ID within the stream of products.")
      bingoProduct <- productDAO.readById(ProductId("X716-6172862"))
      _ <- IO.println("Bingo product: " + bingoProduct)

      _ <- IO.println(s"Finding a service by ID within the stream of services.")
      bingoService <- serviceDAO.readById(ServiceId("Y274-1029755"))
      _ <- IO.println("Bingo service: " + bingoService)

      _ <- IO.println(s"Finding several products by their IDs within the stream of products.")
      severalProducts <- productDAO.readByIds(List(ProductId("O184-6903943"), ProductId("N180-3300253"))).compile.last
      _ <- IO.println("Several products: " + severalProducts)

      _ <- IO.println("[Streamer] Processing the full organisation. This includes resolving the linked products and services.")
      organisation <- fullOrganisationDAO.readAll
      _ <- IO.println("Full Organisation: " + organisation)
      organisationGraph = {
        println("Convert the organisation object into a data graph")

        // XXX Notice the side-effects, here!
        // Here, the organisation graph is not only returned explicitly, but also saved implicitly (mutation) into
        // GLOBAL.model from OLGA.
        val organisationGraph: IOrganisation = ObjectGraphMappings.OrganisationMappings.objectToGraph(organisation.get)

        val out = new FileOutputStream(organisationFile)
        try {
          println(s"Writing organisation data graph to file path '$organisationFile' in RDF/Turtle.")
          Rio.write(GLOBAL.model, out, RDFFormat.TURTLE)
        }
        finally {
          out.close()
        }

        organisationGraph
      }
      _ <- IO.println("Full Organisation graph: " + organisationGraph.iri())
      organisationFromGraphBasedDAO <- {
        val organisationGraphBasedDAO: FullOrganisationTypeLevelEffectfulDAO =
          FullOrganisationTypeLevelEffectfulGraphDAO(graph = () => organisationGraph)

        val organisationsFromGraphBasedDAO: IO[Option[FullOrganisation]] = organisationGraphBasedDAO.readAll

        organisationsFromGraphBasedDAO
      }
      _ <- IO.println("Full Organisation, obtained from via the graph-based DAO: " + organisationFromGraphBasedDAO)
      _ <- IO.println("End of Streamer.")
    } yield ExitCode.Success
  }
}

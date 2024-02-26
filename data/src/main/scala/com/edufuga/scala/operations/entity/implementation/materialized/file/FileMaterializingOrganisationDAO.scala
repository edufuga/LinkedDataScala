package com.edufuga.scala.operations.entity.implementation.materialized.file

import com.edufuga.scala.entities.Organisation
import com.edufuga.scala.operations.entity.implementation.OrganisationMaterializedDAO
import com.edufuga.scala.data.parsers.xml.XMLParsers
import com.edufuga.scala.operations.entity.implementation.ops.FileOps

import scala.xml.XML.loadFile

case class FileMaterializingOrganisationDAO(file: String) extends OrganisationMaterializedDAO {
  override def readAll: Option[Organisation] = XMLParsers.organisation(loadFile(FileOps.pathOf(file).toString))
}

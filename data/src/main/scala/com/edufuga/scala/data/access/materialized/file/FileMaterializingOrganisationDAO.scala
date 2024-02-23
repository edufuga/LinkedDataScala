package com.edufuga.scala.data.access.materialized.file

import com.edufuga.scala.entities.Organisation
import com.edufuga.scala.data.access.entities.OrganisationMaterializedDAO
import com.edufuga.scala.data.access.ops.FileOps
import com.edufuga.scala.data.parsers.xml.XMLParsers

import scala.xml.XML.loadFile

case class FileMaterializingOrganisationDAO(file: String) extends OrganisationMaterializedDAO {
  override def readAll: Option[Organisation] = XMLParsers.organisation(loadFile(FileOps.pathOf(file).toString))
}

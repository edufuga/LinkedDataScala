package com.edufuga.scala.data.access.materialized.file

import com.edufuga.scala.core.Organisation
import com.edufuga.scala.data.access.materialized.OrganisationDAO
import com.edufuga.scala.data.access.ops.FileOps
import com.edufuga.scala.data.parsers.xml.XMLParsers

import scala.xml.XML.loadFile

case class FileOrganisationDAO(file: String) extends OrganisationDAO {
  override def readAll: Option[Organisation] = XMLParsers.organisation(loadFile(FileOps.pathOf(file).toString))
}

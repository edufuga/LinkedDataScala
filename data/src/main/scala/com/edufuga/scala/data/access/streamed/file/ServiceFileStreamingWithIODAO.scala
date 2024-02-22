package com.edufuga.scala.data.access.streamed.file

import com.edufuga.scala.core.Service
import com.edufuga.scala.core.ServiceTypes.ServiceId
import com.edufuga.scala.data.access.entities.ServiceTypeLevelStreamingEffectfulDAO
import com.edufuga.scala.data.parsers.csv.CSVParsers

class ServiceFileStreamingWithIODAO(file: String) 
  extends FileStreamingWithIODAO[ServiceId, Service](file, CSVParsers.service), // how (mechanism: file, streaming, IO)
    ServiceTypeLevelStreamingEffectfulDAO // what (Service DAO)

package com.edufuga.scala.operations.entity.implementation.streamed.file

import com.edufuga.scala.entities.Service
import com.edufuga.scala.entities.ServiceTypes.ServiceId
import com.edufuga.scala.operations.entity.implementation.ServiceTypeLevelEffectfulStreamingDAO
import com.edufuga.scala.data.parsers.csv.CSVParsers

class ServiceFileStreamingWithIODAO(file: String) 
  extends FileStreamingWithIODAO[ServiceId, Service](file, CSVParsers.service), // how (mechanism: file, streaming, IO)
    ServiceTypeLevelEffectfulStreamingDAO // what (Service DAO)

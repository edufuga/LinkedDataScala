package com.edufuga.scala.operations.entity.implementation.streamed.file

import com.edufuga.scala.entities.Product
import com.edufuga.scala.entities.ProductTypes.ProductId
import com.edufuga.scala.entity.parsers.csv.CSVParsers
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.ProductTypeLevelEffectfulStreamingDAO

class ProductFileStreamingWithIODAO(file: String)
  extends FileStreamingWithIODAO[ProductId, Product](file, CSVParsers.product), // how (mechanism: file, streaming, IO)
    ProductTypeLevelEffectfulStreamingDAO // what (Product DAO).

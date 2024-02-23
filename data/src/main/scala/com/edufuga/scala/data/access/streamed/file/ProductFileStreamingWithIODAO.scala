package com.edufuga.scala.data.access.streamed.file

import com.edufuga.scala.entities.Product
import com.edufuga.scala.entities.ProductTypes.ProductId
import com.edufuga.scala.data.access.entities.ProductTypeLevelEffectfulStreamingDAO
import com.edufuga.scala.data.parsers.csv.CSVParsers

class ProductFileStreamingWithIODAO(file: String)
  extends FileStreamingWithIODAO[ProductId, Product](file, CSVParsers.product), // how (mechanism: file, streaming, IO)
    ProductTypeLevelEffectfulStreamingDAO // what (Product DAO).

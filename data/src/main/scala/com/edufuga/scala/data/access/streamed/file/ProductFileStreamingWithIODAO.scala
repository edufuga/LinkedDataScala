package com.edufuga.scala.data.access.streamed.file

import com.edufuga.scala.core.Product
import com.edufuga.scala.core.ProductTypes.ProductId
import com.edufuga.scala.data.access.entities.ProductTypeLevelStreamingEffectfulDAO
import com.edufuga.scala.data.parsers.csv.CSVParsers

class ProductFileStreamingWithIODAO(file: String)
  extends FileStreamingWithIODAO[ProductId, Product](file, CSVParsers.product), // how (mechanism: file, streaming, IO)
    ProductTypeLevelStreamingEffectfulDAO // what (Product DAO).

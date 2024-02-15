package com.edufuga.scala.data.access.streamed

import cats.effect.IO
import com.edufuga.scala.core.Product
import com.edufuga.scala.core.ProductTypes.ProductId

/**
 * Data access to Products.
 * 
 */
trait StreamingProductsDAO extends StreamingDAO[ProductId, IO, Option[Product]]

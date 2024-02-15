package com.edufuga.scala.data.access.streamed

import com.edufuga.scala.core.Product
import com.edufuga.scala.core.ProductTypes.ProductId

/**
 * Data access to Products.
 * 
 */
trait StreamingProductsDAO extends StreamingDAO[ProductId, Option[Product]]

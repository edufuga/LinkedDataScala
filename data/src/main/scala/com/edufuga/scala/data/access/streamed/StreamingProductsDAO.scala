package com.edufuga.scala.data.access.streamed

import com.edufuga.scala.core.Product

/**
 * Data access to Products.
 * 
 */
trait StreamingProductsDAO extends StreamingDAO[Option[Product]]

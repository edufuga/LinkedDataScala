package com.edufuga.scala.data.access.streamed

import com.edufuga.scala.core.Product

/**
 * Data access to Products.
 * 
 */
trait StreamedProductsDAO extends StreamingReadAll[Option[Product]]

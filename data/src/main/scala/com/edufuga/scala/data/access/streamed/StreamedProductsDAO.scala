package com.edufuga.scala.data.access.streamed

import cats.effect.IO
import com.edufuga.scala.core.Product
import com.edufuga.scala.data.access.ReadAll
import fs2.Stream

/**
 * Data access to Products.
 * 
 */
trait StreamedProductsDAO extends ReadAll[Stream[IO, Option[Product]]]

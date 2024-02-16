package com.edufuga.scala.data.access.entities

import com.edufuga.scala.core.Product
import com.edufuga.scala.core.ProductTypes.ProductId
import com.edufuga.scala.data.access.DAO

/**
 * Product DAO, generic in a wrapper type with only one parameter.
 *
 * Notice the _limitation_ imposed by the usage of higher-kinded types: The wrapper type 'W' is a simple wrapper around
 * the concrete Product type. Essentially, (a core functionality of) the ProductDAO is a function from ProductID to
 * (wrapped) Products. The limitation consists of not being able to specify the "DAO for the Product" (with the Product
 * as a specific type and the rest being generic and thus flexible). This is NOT possible, because the wrapper is only
 * going to work with ONE type parameter. We can't specify a flexible or varying or unknown number of type parameters in
 * the higher-kinded type 'W'. At least it's not possible to the best of my (current) knowledge.
 */
trait ProductDAO[+W[+_]] extends DAO[ProductId, W[Product]]

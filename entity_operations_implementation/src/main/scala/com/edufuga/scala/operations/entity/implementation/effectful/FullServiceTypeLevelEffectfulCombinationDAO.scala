package com.edufuga.scala.operations.entity.implementation.effectful

import cats.effect.IO
import cats.implicits.*

import com.edufuga.scala.entities.ProductTypes.ProductId
import com.edufuga.scala.entities.{FullService, Product, Service}
import com.edufuga.scala.operations.entity.implementation.EntityOperationImplementationTypes.FullServiceTypeLevelEffectfulStreamingDAO

sealed class FullServiceTypeLevelEffectfulCombinationDAO(
  productsFromIds: List[ProductId] => IO[List[Product]],
  servicesFromIds: () => IO[List[Service]]
) extends FullServiceTypeLevelEffectfulStreamingDAO {
  override def readAll: IO[List[FullService]] = {
    def toEvalFullService(service: Service): IO[FullService] = {
      val evalProductsOfServiceInDepartment: IO[List[Product]] = productsFromIds(service.products)

      val evalFullService = evalProductsOfServiceInDepartment.map { productsInService =>
        FullService(
          id = service.id,
          serviceName = service.serviceName,
          products = productsInService,
          productManager = service.productManager,
          price = service.price
        )
      }

      evalFullService
    }

    val evalFullServices: IO[List[FullService]] = servicesFromIds().flatMap { (services: List[Service]) =>
      val fullServiceEvalList: List[IO[FullService]] = services.map { (service: Service) =>
        toEvalFullService(service)
      }

      // Convert the List[IO[...]] to IO[List[...]]
      fullServiceEvalList.sequence
    }

    evalFullServices
  }
}

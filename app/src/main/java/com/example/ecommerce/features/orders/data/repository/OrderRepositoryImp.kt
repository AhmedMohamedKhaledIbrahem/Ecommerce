package com.example.ecommerce.features.orders.data.repository

import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.orders.data.data_source.local.OrderLocalDataSource
import com.example.ecommerce.features.orders.data.data_source.remote.OrderRemoteDataSource
import com.example.ecommerce.features.orders.data.mapper.OrderMapper
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity
import com.example.ecommerce.features.orders.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImp @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource,
    private val localDataSource: OrderLocalDataSource,
    private val internetConnectionChecker: InternetConnectionChecker,
) : OrderRepository {
    override suspend fun createOrder(orderRequestEntity: OrderRequestEntity): OrderResponseEntity {
        return try {
            if (internetConnectionChecker.hasConnection()) {
                val orderRequestModel =
                    OrderMapper.mapEntityToModel(entity = orderRequestEntity)
                val orderResponseModel =
                    remoteDataSource.createOrder(orderRequestModel = orderRequestModel)
                OrderMapper.mapModelToEntity(orderResponseModel)

            } else {
                throw Failures.ConnectionFailure(resourceId = R.string.no_internet_connection)
            }
        } catch (failure: FailureException) {
            throw Failures.ServerFailure("${failure.message}")
        }

    }

    override suspend fun getOrders(): List<OrderWithItems> {
        return try {
            localDataSource.getOrders()
        } catch (failure: FailureException) {
            throw Failures.CacheFailure("${failure.message}")
        }

    }

    override suspend fun saveOrderLocally(orderResponseEntity: OrderResponseEntity) {
        try {
            val orderResponseModel = OrderMapper.mapEntityToModel(entity = orderResponseEntity)
            val imagesId = mutableListOf<Int>()
            orderResponseModel.lineItems.map { lineItem ->
                imagesId.add(lineItem.productId)
            }
            val images = localDataSource.getImagesByProductId(imagesId)

            localDataSource.insertOrderWithItem(
                orderResponseModel = orderResponseModel,
                image = images
            )
        } catch (failure: FailureException) {
            throw Failures.CacheFailure("${failure.message}")
        }

    }

    override suspend fun fetchOrders() {
        try {
            if (!internetConnectionChecker.hasConnection()) {
                throw Failures.ConnectionFailure(resourceId = R.string.no_internet_connection)
            }
            val orders = remoteDataSource.getOrders()
            orders.map { order ->
                val imagesId = mutableListOf<Int>()
                order.lineItems.map { lineItem ->
                    imagesId.add(lineItem.productId)
                }
                val images = localDataSource.getImagesByProductId(imagesId)
                localDataSource.insertOrderWithItem(
                    orderResponseModel = order,
                    image = images
                )
            }
        } catch (failure: FailureException) {
            throw Failures.CacheFailure("${failure.message}")
        }
    }

    override suspend fun clearOrders() {

        try {
            localDataSource.clearOrders()
        } catch (failure: FailureException) {
            throw Failures.CacheFailure("${failure.message}")
        }

    }
}
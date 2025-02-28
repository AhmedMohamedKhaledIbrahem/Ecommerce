package com.example.ecommerce.features.orders.data.repository

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderRepositoryImp @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource,
    private val localDataSource: OrderLocalDataSource,
    private val internetConnectionChecker: InternetConnectionChecker,
) : OrderRepository {
    override suspend fun createOrder(orderRequestEntity: OrderRequestEntity): OrderResponseEntity {
        return withContext(Dispatchers.IO) {
            try {
                if (internetConnectionChecker.hasConnection()) {
                    val orderRequestModel =
                        OrderMapper.mapEntityToModel(entity = orderRequestEntity)
                    val orderResponseModel =
                        remoteDataSource.createOrder(orderRequestModel = orderRequestModel)
                    OrderMapper.mapModelToEntity(orderResponseModel)

                } else {
                    throw Failures.ConnectionFailure("No Internet Connection")
                }
            } catch (failure: FailureException) {
                throw Failures.ServerFailure("${failure.message}")
            }
        }
    }

    override suspend fun getOrders(): List<OrderWithItems> {
        return withContext(Dispatchers.IO) {
            try {
                localDataSource.getOrders()
            } catch (failure: FailureException) {
                throw Failures.CacheFailure("${failure.message}")
            }
        }
    }

    override suspend fun saveOrderLocally(orderResponseEntity: OrderResponseEntity) {
        withContext(Dispatchers.IO) {
            try {
                val orderResponseModel = OrderMapper.mapEntityToModel(entity = orderResponseEntity)
                localDataSource.insertOrderWithItem(orderResponseModel = orderResponseModel)
            } catch (failure: FailureException) {
                throw Failures.CacheFailure("${failure.message}")
            }
        }

    }

    override suspend fun clearOrders() {
        withContext(Dispatchers.IO) {
            try {
                localDataSource.clearOrders()
            } catch (failure: FailureException) {
                throw Failures.CacheFailure("${failure.message}")
            }
        }
    }
}
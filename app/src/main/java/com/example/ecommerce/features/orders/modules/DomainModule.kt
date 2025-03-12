package com.example.ecommerce.features.orders.modules

import com.example.ecommerce.features.orders.domain.repository.OrderRepository
import com.example.ecommerce.features.orders.domain.use_case.clear_orders.ClearOrderUseCase
import com.example.ecommerce.features.orders.domain.use_case.clear_orders.IClearOrderUseCase
import com.example.ecommerce.features.orders.domain.use_case.create_order.CreateOrderUseCase
import com.example.ecommerce.features.orders.domain.use_case.create_order.ICreateOrderUseCase
import com.example.ecommerce.features.orders.domain.use_case.get_orders.GetOrdersUseCase
import com.example.ecommerce.features.orders.domain.use_case.get_orders.IGetOrdersUseCase
import com.example.ecommerce.features.orders.domain.use_case.save_order_locally.ISaveOrderLocallyUseCase
import com.example.ecommerce.features.orders.domain.use_case.save_order_locally.SaveOrderLocallyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideCreateOrderUseCase(orderRepository: OrderRepository): ICreateOrderUseCase {
        return CreateOrderUseCase(orderRepository = orderRepository)
    }

    @Provides
    @Singleton
    fun provideGetOrdersUseCase(orderRepository: OrderRepository): IGetOrdersUseCase {
        return GetOrdersUseCase(orderRepository = orderRepository)
    }

    @Provides
    @Singleton
    fun provideClearOrderUseCase(orderRepository: OrderRepository): IClearOrderUseCase {
        return ClearOrderUseCase(orderRepository = orderRepository)
    }

    @Provides
    @Singleton
    fun provideSaveOrderLocallyUseCase(orderRepository: OrderRepository): ISaveOrderLocallyUseCase {
        return SaveOrderLocallyUseCase(repository = orderRepository)
    }
}
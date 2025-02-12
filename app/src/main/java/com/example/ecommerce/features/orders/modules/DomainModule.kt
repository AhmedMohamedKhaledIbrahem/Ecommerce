package com.example.ecommerce.features.orders.modules

import com.example.ecommerce.features.orders.domain.repository.OrderRepository
import com.example.ecommerce.features.orders.domain.use_case.create_order.CreateOrderUseCase
import com.example.ecommerce.features.orders.domain.use_case.create_order.ICreateOrderUseCase
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
}
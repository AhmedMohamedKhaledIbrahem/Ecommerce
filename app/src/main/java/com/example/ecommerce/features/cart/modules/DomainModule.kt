package com.example.ecommerce.features.cart.modules

import com.example.ecommerce.features.cart.domain.repository.CartRepository
import com.example.ecommerce.features.cart.domain.use_case.add_item.AddItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.add_item.AddItemUseCaseImp
import com.example.ecommerce.features.cart.domain.use_case.get_cart.GetCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart.GetCartUseCaseImp
import com.example.ecommerce.features.cart.domain.use_case.remove_Item.RemoveItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.remove_Item.RemoveItemUseCaseImp
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
    fun provideAddItemUseCase(cartRepository: CartRepository): AddItemUseCase {
        return AddItemUseCaseImp(cartRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveItemUseCase(cartRepository: CartRepository): RemoveItemUseCase {
        return RemoveItemUseCaseImp(cartRepository)
    }

    @Provides
    @Singleton
    fun provideGetCartUseCase(cartRepository: CartRepository): GetCartUseCase {
        return GetCartUseCaseImp(cartRepository)

    }


}
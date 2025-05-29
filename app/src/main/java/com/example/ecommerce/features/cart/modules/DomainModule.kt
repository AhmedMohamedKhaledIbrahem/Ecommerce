package com.example.ecommerce.features.cart.modules

import com.example.ecommerce.features.cart.domain.repository.CartRepository
import com.example.ecommerce.features.cart.domain.use_case.add_item.AddItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.add_item.IAddItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.clear_cart.ClearCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.clear_cart.IClearCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart.GetCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart.IGetCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart_count.IGetCartCountUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart_count.GetCartCountUseCase
import com.example.ecommerce.features.cart.domain.use_case.remove_Item.IRemoveItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.remove_Item.RemoveItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.update_item_cart.IUpdateItemsCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.update_item_cart.UpdateItemsCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.update_quantity.IUpdateQuantityUseCase
import com.example.ecommerce.features.cart.domain.use_case.update_quantity.UpdateQuantityUseCase
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
    fun provideAddItemUseCase(cartRepository: CartRepository): IAddItemUseCase {
        return AddItemUseCase(cartRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveItemUseCase(cartRepository: CartRepository): IRemoveItemUseCase {
        return RemoveItemUseCase(cartRepository)
    }

    @Provides
    @Singleton
    fun provideGetCartUseCase(cartRepository: CartRepository): IGetCartUseCase {
        return GetCartUseCase(cartRepository)

    }

    @Provides
    @Singleton
    fun provideUpdateItemCartUseCase(cartRepository: CartRepository): IUpdateItemsCartUseCase {
        return UpdateItemsCartUseCase(cartRepository)

    }

    @Provides
    @Singleton
    fun provideUpdateQuantityUseCase(cartRepository: CartRepository): IUpdateQuantityUseCase {
        return UpdateQuantityUseCase(cartRepository)
    }

    @Provides
    @Singleton
    fun provideClearCartUseCase(cartRepository: CartRepository): IClearCartUseCase {
        return ClearCartUseCase(cartRepository)

    }

    @Provides
    @Singleton
    fun provideGetCartCountUseCase(cartRepository: CartRepository): IGetCartCountUseCase {
        return GetCartCountUseCase(cartRepository)

    }


}
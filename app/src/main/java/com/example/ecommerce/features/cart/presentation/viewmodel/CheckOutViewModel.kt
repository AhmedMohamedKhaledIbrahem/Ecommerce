package com.example.ecommerce.features.cart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.CustomerOrAddressNotFound
import com.example.ecommerce.core.constants.OrderCreatedSuccessfully
import com.example.ecommerce.core.constants.PaymentMethod
import com.example.ecommerce.core.constants.PaymentMethodTitle
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.address.data.mapper.AddressMapper
import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.usecases.getselectaddress.IGetSelectAddressUseCase
import com.example.ecommerce.features.cart.data.mapper.CartMapper
import com.example.ecommerce.features.cart.data.mapper.ItemMapper
import com.example.ecommerce.features.cart.domain.use_case.clear_cart.IClearCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart.IGetCartUseCase
import com.example.ecommerce.features.cart.presentation.event.CheckOutEvent
import com.example.ecommerce.features.cart.presentation.state.CheckOutState
import com.example.ecommerce.features.orders.domain.entities.LineItemRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.use_case.create_order.ICreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckOutViewModel @Inject constructor(
    private val getSelectAddressUseCase: IGetSelectAddressUseCase,
    private val getCartUseCase: IGetCartUseCase,
    private val createOrderUseCase: ICreateOrderUseCase,
    private val clearCartUseCase: IClearCartUseCase,
) : ViewModel() {
    private val _checkOutEvent: Channel<UiEvent> = Channel()
    val checkOutEvent = _checkOutEvent.receiveAsFlow()
    private val _checkOutState = MutableStateFlow(CheckOutState())
    val checkOutState = _checkOutState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = CheckOutState()
    )

    fun onEvent(event: CheckOutEvent) {
        when (event) {
            is CheckOutEvent.Input.AddressId -> {
                _checkOutState.update { it.copy(addressId = event.addressId) }
            }

            is CheckOutEvent.Input.CustomerId -> {
                _checkOutState.update { it.copy(customerId = event.customerId) }
            }

            is CheckOutEvent.CheckoutButton -> {
                checkOut()
            }
        }
    }

    private fun checkOut() {

        viewModelScope.launch {
            try {
                val customerId = checkOutState.value.customerId
                val addressId = checkOutState.value.addressId
                _checkOutState.update { it.copy(isCheckingOut = true) }
                if (customerId == -1 && addressId == -1) {
                    _checkOutEvent.send(UiEvent.ShowSnackBar(CustomerOrAddressNotFound))
                    return@launch
                }
                val (billingAddress, items) = parallelFetch(addressId)
                createOrderUseCase(
                    orderRequestEntity = OrderRequestEntity(
                        customerId = customerId,
                        billing = billingAddress,
                        lineItems = items,
                        paymentMethod = PaymentMethod,
                        paymentMethodTitle = PaymentMethodTitle,
                        setPaid = false,
                    )
                )
                clearCartUseCase.invoke()
                _checkOutEvent.send(
                    UiEvent.CombinedEvents(
                        listOf(
                            UiEvent.ShowSnackBar(OrderCreatedSuccessfully),
                            UiEvent.Navigation.Orders(destinationId = R.id.ordersFragment)
                        )
                    )
                )
            } catch (failure: Failures) {
                val mapFailureMessage = mapFailureMessage(failure)
                _checkOutEvent.send(UiEvent.ShowSnackBar(mapFailureMessage))
            } catch (e: Exception) {
                _checkOutEvent.send(UiEvent.ShowSnackBar(e.message ?: "Unknown error"))
            } finally {
                _checkOutState.update { it.copy(isCheckingOut = false) }
            }
        }


    }
    private suspend fun parallelFetch(addressId: Int): Pair<BillingInfoRequestEntity, List<LineItemRequestEntity>> =
        coroutineScope {
            val addressDeferred = async { getSelectAddressUseCase(addressId) }
            val cartDeferred = async { getCartUseCase() }

            val billing = AddressMapper.mapCustomerAddressEntityToBillingInfoRequest(addressDeferred.await())
            val items = ItemMapper.mapCartWithItemsToLineItemRequest(cartDeferred.await())
            billing to items
        }
}


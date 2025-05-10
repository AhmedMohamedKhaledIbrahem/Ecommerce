package com.example.ecommerce.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.core.ui.event.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class MainNavigationViewModel : ViewModel() {
    private val _navigationEvent: Channel<UiEvent> = Channel()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onBottomNavigationEvent(itemId: Int) {
        viewModelScope.launch {
            try {
                when (itemId) {
                    R.id.productFragment -> {
                        _navigationEvent.send(
                            UiEvent.Navigation.Home(R.id.productFragment)
                        )
                    }

                    R.id.cartFragment -> {
                        _navigationEvent.send(
                            UiEvent.Navigation.Cart(R.id.cartFragment)
                        )
                    }

                    R.id.ordersFragment -> {
                        _navigationEvent.send(
                            UiEvent.Navigation.Orders(R.id.ordersFragment)
                        )
                    }

                    R.id.settingFragment -> {
                        _navigationEvent.send(
                            UiEvent.Navigation.Setting(R.id.settingFragment)
                        )
                    }

                }

            } catch (e: Exception) {
                _navigationEvent.send(UiEvent.ShowSnackBar("${e.message}"))
            }
        }
    }
}
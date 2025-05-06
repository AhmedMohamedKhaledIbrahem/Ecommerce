package com.example.ecommerce.core.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class MainNavigationViewModel : ViewModel(){
    private val _navigationEvent: Channel<NavigationEvent> = Channel()
    val navigationEvent = _navigationEvent.receiveAsFlow()
}
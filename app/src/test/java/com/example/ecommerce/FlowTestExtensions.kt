package com.example.ecommerce

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


fun CoroutineScope.activateTestFlow(stateFlow: StateFlow<*>) = launch {
    stateFlow.collect {}
}
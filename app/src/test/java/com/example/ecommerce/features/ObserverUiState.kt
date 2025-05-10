package com.example.ecommerce.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.ecommerce.core.ui.state.UiState
import java.util.concurrent.CountDownLatch
import kotlin.test.assertEquals

fun observerViewModelSuccessState(
    latch: CountDownLatch,
    expected: Any,
    observerState: LiveData<UiState<Any>>
): Observer<UiState<Any>> {
    val observer = Observer<UiState<Any>> { state ->
        if (state is UiState.Success) {
            assertEquals(
                expected,
                state.data
            )
            latch.countDown()
        }
    }
    observerState.observeForever(observer)
    return observer
}

fun observerViewModelErrorState(
    latch: CountDownLatch,
    expectedException: String,
    observerState: LiveData<UiState<Any>>
): Observer<UiState<Any>> {
    val observer = Observer<UiState<Any>> { state ->
        if (state is UiState.Error) {
            assertEquals(
                expectedException,
                state.message
            )
            latch.countDown()
        }
    }
    observerState.observeForever(observer)
    return observer
}
fun removeObserverFromLiveData(
    liveData: LiveData<UiState<Any>>,
    observer: Observer<UiState<Any>>
) {
    liveData.removeObserver(observer)
}
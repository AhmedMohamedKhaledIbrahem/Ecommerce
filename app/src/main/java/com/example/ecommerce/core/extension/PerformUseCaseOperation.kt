package com.example.ecommerce.core.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import kotlinx.coroutines.launch

inline fun ViewModel.performUseCaseOperation(
    crossinline loadingUpdater: (Boolean) -> Unit,
    crossinline useCase: suspend () -> Unit,
    crossinline onFailure: suspend (Failures) -> Unit,
    crossinline onException: suspend (Exception) -> Unit,
    ) {
    viewModelScope.launch {
        try {
            loadingUpdater(true)
            useCase()
        } catch (failure: Failures) {
            onFailure(failure)
        } catch (e: Exception) {
            onException(e)
        } finally {
            loadingUpdater(false)
        }
    }
}
    inline fun ViewModel.performUseCaseOperation(
        crossinline useCase: suspend () -> Unit,
        crossinline onFailure: suspend (Failures) -> Unit,
        crossinline onException: suspend (Exception) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                useCase()
            } catch (failure: Failures) {
                onFailure(failure)
            } catch (e: Exception) {
                onException(e)
            }
        }
    }


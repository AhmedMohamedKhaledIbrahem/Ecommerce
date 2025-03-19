package com.example.ecommerce.features.preferences.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.preferences.domain.usecase.getlanguage.IGetLanguageUseCase
import com.example.ecommerce.features.preferences.domain.usecase.isdarkmodeenabled.IIsDarkModeEnableUseCase
import com.example.ecommerce.features.preferences.domain.usecase.setdarkmodeenable.ISetDarkModeEnableUseCase
import com.example.ecommerce.features.preferences.domain.usecase.setlanguage.ISetLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val getLanguageUseCase: IGetLanguageUseCase,
    private val setLanguageUseCase: ISetLanguageUseCase,
    private val isDarkModeEnableUseCase: IIsDarkModeEnableUseCase,
    private val setDarkModeEnableUseCase: ISetDarkModeEnableUseCase,

    ) : ViewModel(), IPreferencesViewModel {
    private val _preferencesState = MutableSharedFlow<UiState<Any>>(replay = 0)
    override val preferencesState: SharedFlow<UiState<Any>> get() = _preferencesState.asSharedFlow()
    override fun getLanguage() {
        preferencesUiState(
            operation = { getLanguageUseCase.invoke() },
            onSuccess = { result ->
                _preferencesState.emit(UiState.Success(result, "getLanguage"))

            },
            source = "getLanguage"
        )
    }

    override fun setLanguage(languageCode: String) {
        preferencesUiState(
            operation = { setLanguageUseCase.invoke(languageCode) },
            onSuccess = { result ->
                _preferencesState.emit(UiState.Success(result, "setLanguage"))
            },
            source = "setLanguage"
        )
    }

    override fun isDarkModeEnabled() {
        preferencesUiState(
            operation = { isDarkModeEnableUseCase.invoke() },
            onSuccess = { result ->
                _preferencesState.emit(UiState.Success(result, "isDarkModeEnabled"))
            },
            source = "isDarkModeEnabled"
        )
    }

    override fun setDarkModeEnabled(isDarkModeEnabled: Boolean) {
        preferencesUiState(
            operation = { setDarkModeEnableUseCase.invoke(isDarkModeEnabled) },
            onSuccess = { result ->
                _preferencesState.emit(UiState.Success(result, "setDarkModeEnabled"))
            },
            source = "setDarkModeEnabled"
        )
    }

    override fun <T> preferencesUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            _preferencesState.emit(UiState.Loading(source))
            try {
                val result =  operation()
                onSuccess(result)
            } catch (failure: Failures) {
                _preferencesState.emit(
                    UiState.Error(
                        mapFailureMessage(failures = failure),
                        source = source
                    )
                )
            } catch (e: Exception) {
                _preferencesState.emit(
                    UiState.Error(
                        message = e.message ?: "Unknown Error",
                        source = source
                    )
                )
            }
        }
    }
}
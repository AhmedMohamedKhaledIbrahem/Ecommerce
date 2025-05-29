package com.example.ecommerce.features.preferences.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.preferences.domain.usecase.getlanguage.IGetLanguageUseCase
import com.example.ecommerce.features.preferences.domain.usecase.isdarkmodeenabled.IIsDarkModeEnableUseCase
import com.example.ecommerce.features.preferences.domain.usecase.setdarkmodeenable.ISetDarkModeEnableUseCase
import com.example.ecommerce.features.preferences.domain.usecase.setlanguage.ISetLanguageUseCase
import com.example.ecommerce.features.preferences.presentation.event.PreferencesEvent
import com.example.ecommerce.features.preferences.presentation.state.PreferencesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val getLanguageUseCase: IGetLanguageUseCase,
    private val setLanguageUseCase: ISetLanguageUseCase,
    private val isDarkModeEnableUseCase: IIsDarkModeEnableUseCase,
    private val setDarkModeEnableUseCase: ISetDarkModeEnableUseCase,
) : ViewModel() {

    private val _preferencesEvent: Channel<UiEvent> = Channel()
    val preferencesEvent = _preferencesEvent.receiveAsFlow()
    private val _preferencesState = MutableStateFlow(PreferencesState())
    val preferencesState: StateFlow<PreferencesState> get() = _preferencesState.asStateFlow()


    fun onEvent(event: PreferencesEvent) {
        eventHandler(event = event) { evt ->
            when (evt) {
                is PreferencesEvent.Input.SetLanguage -> {
                    _preferencesState.update { it.copy(languageCode = evt.languageCode) }
                }

                is PreferencesEvent.Input.SetDarkMode -> {
                    _preferencesState.update { it.copy(isDarkMode = evt.isDarkMode) }
                }

                is PreferencesEvent.Get.GetLanguage -> {
                    getLanguage()
                }

                is PreferencesEvent.Get.GetDarkMode -> {
                    getDarkMode()
                }

                is PreferencesEvent.Button.DarkModeButton -> {
                    darkModeButton()
                }

                is PreferencesEvent.Button.LanguageButton -> {
                    languageButton()
                }

            }
        }
    }


    fun languageButton() = performUseCaseOperation(
        useCase = {
            val languageCode = preferencesState.value.languageCode
            setLanguageUseCase.invoke(languageCode = languageCode)
            _preferencesState.update { it.copy(isFinished = true) }
        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failures = failure)
            _preferencesEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
        },
        onException = {
            _preferencesEvent.send(UiEvent.ShowSnackBar(it.message ?: Unknown_Error))
        },
    )

    fun getLanguage() = performUseCaseOperation(
        useCase = {
            val languageCode = getLanguageUseCase.invoke()
            _preferencesState.update { it.copy(languageCode = languageCode) }
        },
        onFailure = {
            val mapFailureToMessage = mapFailureMessage(failures = it)
            _preferencesEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
        },
        onException = {
            _preferencesEvent.send(UiEvent.ShowSnackBar(it.message ?: Unknown_Error))
        },
    )

    fun darkModeButton() = performUseCaseOperation(
        useCase = {
            val isDarkMode = preferencesState.value.isDarkMode
            setDarkModeEnableUseCase.invoke(isDarkModeEnabled = isDarkMode)
        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failures = failure)
            _preferencesEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
        },
        onException = {
            _preferencesEvent.send(UiEvent.ShowSnackBar(it.message ?: Unknown_Error))
        }
    )

    fun getDarkMode() = performUseCaseOperation(
        useCase = {
            val isDarkMode = isDarkModeEnableUseCase.invoke()
            _preferencesState.update { it.copy(isDarkMode = isDarkMode) }
        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failures = failure)
            _preferencesEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
        },
        onException = {
            _preferencesEvent.send(UiEvent.ShowSnackBar(it.message ?: Unknown_Error))
        }
    )
}
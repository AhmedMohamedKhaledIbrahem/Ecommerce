package com.example.ecommerce.features.authentication.presentation.viewmodel.forgetpassowrd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.CheckYourEmail
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.usecases.restpassword.IResetPasswordUseCase
import com.example.ecommerce.features.authentication.presentation.event.ForgetPasswordEvent
import com.example.ecommerce.features.authentication.presentation.state.ForgetPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: IResetPasswordUseCase
) : ViewModel() {
    private val _forgetPasswordEvent: Channel<UiEvent> = Channel()
    val forgetPasswordEvent = _forgetPasswordEvent.receiveAsFlow()

    private val _forgetPasswordState = MutableStateFlow(ForgetPasswordState())
    val forgetPasswordState: StateFlow<ForgetPasswordState> = _forgetPasswordState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = ForgetPasswordState()
    )

    fun onEvent(event: ForgetPasswordEvent) {
        when (event) {
            is ForgetPasswordEvent.EmailInput -> {
                _forgetPasswordState.update { it.copy(email = event.value) }
            }

            is ForgetPasswordEvent.ForgetPasswordButton -> {
                resetPassword()
            }
        }

    }

    private fun resetPassword() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _forgetPasswordState.update { it.copy(isLoading = isLoading) }
        },
        useCase = {
            val resetPasswordParams =
                EmailRequestEntity(email = forgetPasswordState.value.email)
            resetPasswordUseCase(resetPasswordParams = resetPasswordParams)
            _forgetPasswordEvent.send(
                UiEvent.CombinedEvents(
                    listOf(
                        UiEvent.ShowSnackBar(resId = R.string.checkYourEmail),
                        UiEvent.Navigation.SignIn(R.id.loginFragment)
                    )
                )
            )
        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failures = failure)
            _forgetPasswordEvent.send(UiEvent.ShowSnackBar(message = mapFailureToMessage))
        },
        onException = {
            _forgetPasswordEvent.send(
                UiEvent.ShowSnackBar(
                    message = it.message ?: Unknown_Error
                )
            )
        }
    )


}

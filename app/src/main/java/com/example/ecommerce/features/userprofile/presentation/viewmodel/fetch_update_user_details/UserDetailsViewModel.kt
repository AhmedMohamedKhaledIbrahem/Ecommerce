package com.example.ecommerce.features.userprofile.presentation.viewmodel.fetch_update_user_details

import androidx.lifecycle.ViewModel
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.userprofile.domain.usecases.fetch_update_user_details.IFetchUpdateUserDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.update_user_details.IUpdateUserDetailsUseCase
import com.example.ecommerce.features.userprofile.presentation.event.UserDetailsEvent
import com.example.ecommerce.features.userprofile.presentation.state.UserDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val fetchUpdateUserDetailsUseCase: IFetchUpdateUserDetailsUseCase,
    private val updateUserDetailsUseCase: IUpdateUserDetailsUseCase,
) : ViewModel() {
    private val _userDetailsEvent: Channel<UiEvent> = Channel()

    val userDetailsEvent = _userDetailsEvent.receiveAsFlow()
    private val _userDetailsState = MutableStateFlow(UserDetailsState())

    val userDetailsState: StateFlow<UserDetailsState>
        get() = _userDetailsState.asStateFlow()

    fun onEvent(event: UserDetailsEvent) {
        eventHandler(event = event) { evt ->
            when (evt) {
                is UserDetailsEvent.LoadUserDetailsCheck -> {
                    fetchUpdateUserDetails()
                }

                is UserDetailsEvent.UserDetailsUpdateButton -> {
                    updateUserDetails()
                }

                is UserDetailsEvent.UserDetailsUpdateInput -> {
                    _userDetailsState.update {
                        it.copy(
                            updateUserDetailsRequestEntity = evt.updateUserDetailsRequestEntity
                        )
                    }
                }
            }
        }
    }

    private fun fetchUpdateUserDetails() = performUseCaseOperation(
        useCase = {
            val displayName = fetchUpdateUserDetailsUseCase()
            _userDetailsState.update { it.copy(displayName = displayName) }
        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failure)
            _userDetailsEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
        },
        onException = {
            _userDetailsEvent.send(UiEvent.ShowSnackBar(it.message ?: Unknown_Error))
        },
    )

    fun updateUserDetails() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _userDetailsState.update { it.copy(isUpdateLoading = isLoading) }
        },
        useCase = {
            val updateUserDetailsParams = userDetailsState.value.updateUserDetailsRequestEntity
            if (updateUserDetailsParams == null) {
                _userDetailsState.update { it.copy(isUpdateLoading = false) }
                return@performUseCaseOperation
            }
            updateUserDetailsUseCase(updateUserDetailsParams)
            _userDetailsEvent.send(
                UiEvent.ShowSnackBar(
                    resId = R.string.update_user_details_success
                )
            )
            _userDetailsState.update { it.copy(isUpdateSuccess = true) }
        },
        onFailure = {
            val mapFailureToMessage = mapFailureMessage(it)
            _userDetailsEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
        },
        onException = {
            _userDetailsEvent.send(UiEvent.ShowSnackBar(it.message ?: Unknown_Error))
        },

        )


}
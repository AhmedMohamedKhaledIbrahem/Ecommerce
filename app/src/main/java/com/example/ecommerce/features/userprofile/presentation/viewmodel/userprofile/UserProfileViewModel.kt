package com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile

import androidx.lifecycle.ViewModel
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.userprofile.domain.usecases.get_user_profile.IGetUserProfileUseCase
import com.example.ecommerce.features.userprofile.presentation.event.UserProfileEvent
import com.example.ecommerce.features.userprofile.presentation.state.UserProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: IGetUserProfileUseCase,
) : ViewModel() {
    private val _userProfileEvent: Channel<UiEvent> = Channel()
    val userProfileEvent = _userProfileEvent.receiveAsFlow()

    private val _userProfileState = MutableStateFlow(UserProfileState())
    val userProfileState: StateFlow<UserProfileState> get() = _userProfileState.asStateFlow()

    fun onEvent(event: UserProfileEvent) {
        eventHandler(event = event) { evt ->
            when (evt) {
                is UserProfileEvent.UserProfileLoad -> {
                    getUserProfile()
                }
            }

        }
    }

    private fun getUserProfile() = performUseCaseOperation(
        useCase = {
            val userProfile = getUserProfileUseCase.invoke()
            _userProfileState.update { it.copy(userEntity = userProfile) }
        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failure)
            _userProfileEvent.send(UiEvent.ShowSnackBar(message = mapFailureToMessage))
        },
        onException = {
            _userProfileEvent.send(UiEvent.ShowSnackBar(message = it.message ?: Unknown_Error))
        },
    )

}
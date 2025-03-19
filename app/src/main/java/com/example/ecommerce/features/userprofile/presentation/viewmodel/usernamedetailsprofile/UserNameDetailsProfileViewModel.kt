package com.example.ecommerce.features.userprofile.presentation.viewmodel.usernamedetailsprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import com.example.ecommerce.features.userprofile.domain.usecases.getusernamedetails.IGetUserNameDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.updateusernamedetails.IUpdateUserNameDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserNameDetailsProfileViewModel @Inject constructor(
    private val getUserNameDetailsUseCase: IGetUserNameDetailsUseCase,
    private val updateUserNameDetailsUseCase: IUpdateUserNameDetailsUseCase,

    ) : ViewModel(), IUserNameDetailsProfileViewModel {
    private val _userNameDetailsProfileState = MutableSharedFlow<UiState<Any>>(replay=0)
    override val userNameDetailsProfileState: SharedFlow<UiState<Any>> get() = _userNameDetailsProfileState.asSharedFlow()

    override fun getUserNameDetails() {
        userNameDetailsProfileUiState(
            operation = { getUserNameDetailsUseCase() },
            onSuccess = { result ->
                _userNameDetailsProfileState.emit(UiState.Success(result, "getUserNameDetails"))
            },
            source = "getUserNameDetails"
        )
    }

    override fun updateUserNameDetails(
        updateUserNameDetailsParams: UpdateUserNameDetailsRequestEntity
    ) {
        userNameDetailsProfileUiState(
            operation = { updateUserNameDetailsUseCase(updateUserNameDetailsParams) },
            onSuccess = { result ->
                _userNameDetailsProfileState.emit(UiState.Success(result, "updateUserNameDetails"))
            },
            source = "updateUserNameDetails"
        )
    }

    override fun <T> userNameDetailsProfileUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            _userNameDetailsProfileState.emit(UiState.Loading(source))
            try {
                val result = operation()
                onSuccess(result)
            } catch (failure: Failures) {
                _userNameDetailsProfileState.emit(UiState.Error(mapFailureMessage(failure), source))

            } catch (e: Exception) {
                _userNameDetailsProfileState.emit(
                    UiState.Error(
                        e.message ?: "Unknown Error",
                        source
                    )
                )

            }
        }

    }
}
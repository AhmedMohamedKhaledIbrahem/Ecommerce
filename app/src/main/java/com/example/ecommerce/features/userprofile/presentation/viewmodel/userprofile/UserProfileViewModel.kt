package com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.userprofile.domain.usecases.getuserprofilebyid.IGetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: IGetUserProfileUseCase,
) : ViewModel(), IUserProfileViewModel {

    private val _userProfileState = MutableSharedFlow<UiState<Any>>(replay = 0)
    override val userProfileState: SharedFlow<UiState<Any>> get() = _userProfileState.asSharedFlow()

    override fun getUserProfile() {
        userProfileUiState(
            operation = { getUserProfileUseCase() },
            onSuccess = { result ->
                _userProfileState.emit(UiState.Success(result, "getUserProfile"))
            },
            source = "getUserProfile"
        )
    }

    override fun <T> userProfileUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            _userProfileState.emit(UiState.Loading(source))
            try {
                val result = withContext(Dispatchers.IO) { operation() }
                onSuccess(result)
            } catch (failure: Failures) {
                _userProfileState.emit(UiState.Error(mapFailureMessage(failure), source))
            } catch (e: Exception) {
                _userProfileState.emit(UiState.Error(e.message ?: "Unknown Error", source))
            }
        }
    }
}
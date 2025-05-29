package com.example.ecommerce.features.userprofile.presentation.viewmodel.imageprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.userprofile.domain.usecases.get_image_profile_by_id.IGetImageProfileByIdUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.upload_image_profile.IUploadImageProfileUseCase
import com.example.ecommerce.features.userprofile.presentation.event.ImageProfileEvent
import com.example.ecommerce.features.userprofile.presentation.state.ImageProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ImageProfileViewModel @Inject constructor(
    private val getImageProfileByIdUseCase: IGetImageProfileByIdUseCase,
    private val uploadImageProfileUseCase: IUploadImageProfileUseCase,

    ) : ViewModel() {
    private val _imageProfileEvent: Channel<UiEvent> = Channel()
    val imageProfileEvent = _imageProfileEvent.receiveAsFlow()

    private val _imageProfileState = MutableStateFlow(ImageProfileState())
    val imageProfileState: StateFlow<ImageProfileState>
        get() = _imageProfileState.asStateFlow()

    fun onEvent(event: ImageProfileEvent) {
        eventHandler(event = event) { evt ->
            when (evt) {
                is ImageProfileEvent.Input.UploadImage -> {
                    _imageProfileState.update { it.copy(file = evt.imageFile) }
                }

                is ImageProfileEvent.Input.GetImage -> {
                    _imageProfileState.update { it.copy(userId = evt.userId) }
                }

                is ImageProfileEvent.UploadImageProfileButton -> {
                    uploadImageProfile()
                }

                is ImageProfileEvent.LoadImageById -> {
                    getImageProfileById()
                }
            }
        }

    }

    private fun uploadImageProfile() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _imageProfileState.update { it.copy(isUploadImageLoading = isLoading) }
        },
        useCase = {
            val file = imageProfileState.value.file
            val uploadImageProfile = uploadImageProfileUseCase(file)
            _imageProfileState.update { it.copy(uploadImageProfileResponseEntity = uploadImageProfile) }
        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failure)
            _imageProfileEvent.send(UiEvent.ShowSnackBar(message = mapFailureToMessage))
        },
        onException = {
            _imageProfileEvent.send(UiEvent.ShowSnackBar(message = it.message ?: Unknown_Error))
        },

        )

    private fun getImageProfileById() = performUseCaseOperation(
        useCase = {
            val userId = imageProfileState.value.userId
            val getImageProfile = getImageProfileByIdUseCase(userId)
            _imageProfileState.update { it.copy(getImageProfileResponseEntity = getImageProfile) }
        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failure)
            _imageProfileEvent.send(UiEvent.ShowSnackBar(message = mapFailureToMessage))
        },
        onException = {
            _imageProfileEvent.send(UiEvent.ShowSnackBar(message = it.message ?: Unknown_Error))
        }
    )


}
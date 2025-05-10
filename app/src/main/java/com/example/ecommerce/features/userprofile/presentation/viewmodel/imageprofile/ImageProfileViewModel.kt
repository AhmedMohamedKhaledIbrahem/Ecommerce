package com.example.ecommerce.features.userprofile.presentation.viewmodel.imageprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.state.UiState
import com.example.ecommerce.features.userprofile.domain.usecases.getimageprofilebyid.IGetImageProfileByIdUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.uploadimageprofile.IUploadImageProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImageProfileViewModel @Inject constructor(
    private val getImageProfileByIdUseCase: IGetImageProfileByIdUseCase,
    private val uploadImageProfileUseCase: IUploadImageProfileUseCase,

    ) : ViewModel(), IImageProfileViewModel {
    private val _imageProfileState = MutableStateFlow<UiState<Any>>(UiState.Ideal)
    override val imageProfileState: StateFlow<UiState<Any>> get() = _imageProfileState

    override  fun uploadImageProfile(image: File) {
        imageProfileUiState(
            operation = { uploadImageProfileUseCase(image = image) },
            onSuccess = { result ->
                _imageProfileState.emit(UiState.Success(result.message, "uploadImageProfile"))
            },
            source = "uploadImageProfile"
        )
    }

    override  fun getImageProfileById(userId: Int) {
        imageProfileUiState(
            operation = { getImageProfileByIdUseCase(userId = userId) },
            onSuccess = { result ->
                _imageProfileState.emit(UiState.Success(result, "getImageProfileById"))
            },
            source = "getImageProfileById"
        )
    }

    override fun <T> imageProfileUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            _imageProfileState.emit(UiState.Loading(source))
            try {
                val result = operation()
                onSuccess(result)
            } catch (failure: Failures) {
                _imageProfileState.emit(UiState.Error(mapFailureMessage(failure), source))
            } catch (e: Exception) {
                _imageProfileState.emit(UiState.Error(e.message ?: "Unknown Error", source))
            }
        }
    }
}
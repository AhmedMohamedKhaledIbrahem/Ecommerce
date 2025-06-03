package com.example.ecommerce.features.category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.category.domain.use_case.get_category.GetCategoryUseCase
import com.example.ecommerce.features.category.domain.use_case.insert_category.InsertCategoryUseCase
import com.example.ecommerce.features.category.presentation.event.CategoryEvent
import com.example.ecommerce.features.category.presentation.state.CategoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val insertCategoryUseCase: InsertCategoryUseCase,
    private val getCategoryUseCase: GetCategoryUseCase
) : ViewModel() {
    private val _categoryEvent: Channel<UiEvent> = Channel()
    val categoryEvent = _categoryEvent.receiveAsFlow()
    private val _categoryState = MutableStateFlow(CategoryState())
    val categoryState: StateFlow<CategoryState> = _categoryState.asStateFlow()

    fun onEvent(event: CategoryEvent) {
        eventHandler(event) { evt ->
            when (evt) {
                is CategoryEvent.LoadCategory -> {
                    loadCategory()
                }

                is CategoryEvent.FetchCategory -> {
                    fetchCategory()
                }
            }
        }
    }

    private fun fetchCategory() = performUseCaseOperation(
        useCase = {
            val categories = getCategoryUseCase.invoke()
            if (categories.isEmpty()) {
                insertCategoryUseCase.invoke()
            }
            _categoryState.update { it.copy(isFetched = true) }

        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failure)
            _categoryEvent.send(
                UiEvent.ShowSnackBar(message = mapFailureToMessage)
            )
        },
        onException = {
            _categoryEvent.send(
                UiEvent.ShowSnackBar(message = it.message ?: Unknown_Error)
            )
        },
    )

    private fun loadCategory() = performUseCaseOperation(
        useCase = {
            val categories = getCategoryUseCase.invoke()
            _categoryState.update { it.copy(categories = categories) }
        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failure)
            _categoryEvent.send(
                UiEvent.ShowSnackBar(message = mapFailureToMessage)
            )
        },
        onException = {
            _categoryEvent.send(
                UiEvent.ShowSnackBar(message = it.message ?: Unknown_Error)
            )
        },
    )


}
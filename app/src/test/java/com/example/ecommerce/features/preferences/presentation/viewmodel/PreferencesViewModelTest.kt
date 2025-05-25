package com.example.ecommerce.features.preferences.presentation.viewmodel

import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.preferences.domain.usecase.getlanguage.IGetLanguageUseCase
import com.example.ecommerce.features.preferences.domain.usecase.isdarkmodeenabled.IIsDarkModeEnableUseCase
import com.example.ecommerce.features.preferences.domain.usecase.setdarkmodeenable.ISetDarkModeEnableUseCase
import com.example.ecommerce.features.preferences.domain.usecase.setlanguage.ISetLanguageUseCase
import com.example.ecommerce.features.preferences.presentation.event.PreferencesEvent
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class PreferencesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getLanguageUseCase = mockk<IGetLanguageUseCase>()
    private val setLanguageUseCase = mockk<ISetLanguageUseCase>()
    private val isDarkModeEnableUseCase = mockk<IIsDarkModeEnableUseCase>()
    private val setDarkModeEnableUseCase = mockk<ISetDarkModeEnableUseCase>()
    private lateinit var viewModel: PreferencesViewModel

    @Before
    fun setup() {
        viewModel = PreferencesViewModel(
            getLanguageUseCase = getLanguageUseCase,
            setLanguageUseCase = setLanguageUseCase,
            isDarkModeEnableUseCase = isDarkModeEnableUseCase,
            setDarkModeEnableUseCase = setDarkModeEnableUseCase
        )
    }

    @Test
    fun `onEvent Input setLanguage should update language Code state`() = runTest {
        val job = activateTestFlow(viewModel.preferencesState)
        val languageCode = "En"
        viewModel.onEvent(PreferencesEvent.Input.SetLanguage(languageCode))
        advanceUntilIdle()
        assertEquals(languageCode, viewModel.preferencesState.value.languageCode)
        job.cancel()
    }

    @Test
    fun `onEvent Input setDarkMode should update isDarkMode state`() = runTest {
        val job = activateTestFlow(viewModel.preferencesState)
        val isDarkMode = true
        viewModel.onEvent(PreferencesEvent.Input.SetDarkMode(isDarkMode))
        advanceUntilIdle()
        assertEquals(isDarkMode, viewModel.preferencesState.value.isDarkMode)
        job.cancel()
    }

    @Test
    fun `onEvent getLanguage should call getLanguageUseCase`() = runTest {
        val preferencesSpy = spyk(viewModel, recordPrivateCalls = true)
        preferencesSpy.onEvent(PreferencesEvent.Get.GetLanguage)
        coVerify(exactly = 1) { preferencesSpy[GET_LANGUAGE]() }
    }

    @Test
    fun `onEvent getDarkMode should call getLanguageUseCase`() = runTest {
        val preferencesSpy = spyk(viewModel, recordPrivateCalls = true)
        preferencesSpy.onEvent(PreferencesEvent.Get.GetDarkMode)
        coVerify(exactly = 1) { preferencesSpy[GET_DARK_MODE]() }
    }

    @Test
    fun `onEvent languageButton should call setLanguageUseCase`() = runTest {
        val preferencesSpy = spyk(viewModel, recordPrivateCalls = true)
        preferencesSpy.onEvent(PreferencesEvent.Button.LanguageButton)
        coVerify(exactly = 1) { preferencesSpy[LANGUAGE_BUTTON]() }
    }

    @Test
    fun `onEvent darkModeButton should call setDarkModeEnableUseCase`() = runTest {
        val preferencesSpy = spyk(viewModel, recordPrivateCalls = true)
        preferencesSpy.onEvent(PreferencesEvent.Button.DarkModeButton)
        coVerify(exactly = 1) { preferencesSpy[DARK_MODE_BUTTON]() }
    }


    @Test
    fun `languageButton should call setLanguageUseCase and update isFinished state`() = runTest {
        val languageCode = "En"
        viewModel.onEvent(PreferencesEvent.Input.SetLanguage(languageCode))
        advanceUntilIdle()
        val languageStateValue = viewModel.preferencesState.value.languageCode
        coEvery { setLanguageUseCase.invoke(languageStateValue) } just Runs
        viewModel.onEvent(PreferencesEvent.Button.LanguageButton)
        advanceUntilIdle()
        coVerify(exactly = 1) { setLanguageUseCase.invoke(languageStateValue) }
        assertTrue(viewModel.preferencesState.value.isFinished)
    }

    @Test
    fun `languageButton should throw failure and send ShowSnackBar event`() = runTest {
        val languageCode = "En"
        viewModel.onEvent(PreferencesEvent.Input.SetLanguage(languageCode))
        advanceUntilIdle()

        val languageStateValue = viewModel.preferencesState.value.languageCode
        coEvery { setLanguageUseCase.invoke(languageStateValue) } throws Failures.CacheFailure(
            errorMessage
        )

        val eventDeferred = async { viewModel.preferencesEvent.first() }
        viewModel.onEvent(PreferencesEvent.Button.LanguageButton)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }

    @Test
    fun `languageButton should throw Exception and send ShowSnackBar event`() = runTest {
        val languageCode = "En"
        viewModel.onEvent(PreferencesEvent.Input.SetLanguage(languageCode))
        advanceUntilIdle()

        val languageStateValue = viewModel.preferencesState.value.languageCode
        coEvery { setLanguageUseCase.invoke(languageStateValue) } throws Exception(
            errorMessage
        )

        val eventDeferred = async { viewModel.preferencesEvent.first() }
        viewModel.onEvent(PreferencesEvent.Button.LanguageButton)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }

    @Test
    fun `darkModeButton should call setDarkModeEnableUseCase`() = runTest {
        val isDarkMode = true
        viewModel.onEvent(PreferencesEvent.Input.SetDarkMode(isDarkMode))
        advanceUntilIdle()
        val darkModeStateValue = viewModel.preferencesState.value.isDarkMode
        coEvery { setDarkModeEnableUseCase.invoke(darkModeStateValue) } just Runs
        viewModel.onEvent(PreferencesEvent.Button.DarkModeButton)
        advanceUntilIdle()
        coVerify(exactly = 1) { setDarkModeEnableUseCase.invoke(darkModeStateValue) }

    }

    @Test
    fun `darkModeButton should throw failure and send ShowSnackBar event`() = runTest {
        val isDarkMode = true
        viewModel.onEvent(PreferencesEvent.Input.SetDarkMode(isDarkMode))
        advanceUntilIdle()

        val darkModeStateValue = viewModel.preferencesState.value.isDarkMode
        coEvery { setDarkModeEnableUseCase.invoke(darkModeStateValue) } throws Failures.CacheFailure(
            errorMessage
        )

        val eventDeferred = async { viewModel.preferencesEvent.first() }
        viewModel.onEvent(PreferencesEvent.Button.DarkModeButton)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }

    @Test
    fun `darkModeButton should throw Exception and send ShowSnackBar event`() = runTest {
        val isDarkMode = true
        viewModel.onEvent(PreferencesEvent.Input.SetDarkMode(isDarkMode))
        advanceUntilIdle()

        val darkModeStateValue = viewModel.preferencesState.value.isDarkMode
        coEvery { setDarkModeEnableUseCase.invoke(darkModeStateValue) } throws Exception(
            errorMessage
        )

        val eventDeferred = async { viewModel.preferencesEvent.first() }
        viewModel.onEvent(PreferencesEvent.Button.DarkModeButton)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }

    @Test
    fun `getLanguage should call getLanguageUseCase and update  state`() = runTest {
        val languageCode = "En"
        coEvery { getLanguageUseCase.invoke() } returns languageCode
        viewModel.onEvent(PreferencesEvent.Get.GetLanguage)
        advanceUntilIdle()
        coVerify(exactly = 1) { getLanguageUseCase.invoke() }
        assertEquals(languageCode, viewModel.preferencesState.value.languageCode)
    }

    @Test
    fun `getLanguage should throw failure and send ShowSnackBar event`() = runTest {
        coEvery { getLanguageUseCase.invoke() } throws Failures.CacheFailure(
            errorMessage
        )

        val eventDeferred = async { viewModel.preferencesEvent.first() }
        viewModel.onEvent(PreferencesEvent.Get.GetLanguage)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }

    @Test
    fun `getLanguage should throw Exception and send ShowSnackBar event`() = runTest {
        coEvery { getLanguageUseCase.invoke() } throws Exception(
            errorMessage
        )
        val eventDeferred = async { viewModel.preferencesEvent.first() }
        viewModel.onEvent(PreferencesEvent.Get.GetLanguage)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }

    @Test
    fun `getDarkMode should call getDarkModeEnableUseCase and update state`() = runTest {
        val isDarkModeEnable = true
        coEvery { isDarkModeEnableUseCase.invoke() } returns isDarkModeEnable
        viewModel.onEvent(PreferencesEvent.Get.GetDarkMode)
        advanceUntilIdle()
        coVerify(exactly = 1) { isDarkModeEnableUseCase.invoke() }
        assertEquals(isDarkModeEnable, viewModel.preferencesState.value.isDarkMode)
    }

    @Test
    fun `getDarkMode should throw failure and send ShowSnackBar event`() = runTest {

        coEvery { isDarkModeEnableUseCase.invoke() } throws Failures.CacheFailure(
            errorMessage
        )

        val eventDeferred = async { viewModel.preferencesEvent.first() }
        viewModel.onEvent(PreferencesEvent.Get.GetDarkMode)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }

    @Test
    fun `getDarkMode should throw Exception and send ShowSnackBar event`() = runTest {

        coEvery { isDarkModeEnableUseCase.invoke() } throws Exception(
            errorMessage
        )

        val eventDeferred = async { viewModel.preferencesEvent.first() }
        viewModel.onEvent(PreferencesEvent.Get.GetDarkMode)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }


    companion object {
        private const val GET_LANGUAGE = "getLanguage"
        private const val GET_DARK_MODE = "getDarkMode"
        private const val LANGUAGE_BUTTON = "languageButton"
        private const val DARK_MODE_BUTTON = "darkModeButton"


    }

}
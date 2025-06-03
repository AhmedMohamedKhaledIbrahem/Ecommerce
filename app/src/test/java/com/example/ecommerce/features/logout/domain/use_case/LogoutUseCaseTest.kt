package com.example.ecommerce.features.logout.domain.use_case

import com.example.ecommerce.features.logout.domain.repository.LogoutRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class LogoutUseCaseTest {
    private val repository = mockk<LogoutRepository>()
    private lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun setup() {
        logoutUseCase = LogoutUseCaseImp(repository)
    }

    @Test
    fun `logout should call repository logout`() = runTest {
        val token = "abc12:ab"
        coEvery { repository.logout(token) } just Runs
        logoutUseCase.invoke(token)
        coVerify(exactly = 1) { repository.logout(token) }

    }

}
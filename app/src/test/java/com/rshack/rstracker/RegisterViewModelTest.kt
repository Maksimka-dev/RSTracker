package com.rshack.rstracker

import com.rshack.rstracker.model.repository.IAuthenticationRepository
import com.rshack.rstracker.utils.AuthUiState
import com.rshack.rstracker.utils.Result
import com.rshack.rstracker.viewmodel.LoginViewModel
import com.rshack.rstracker.viewmodel.RegisterViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.Exception
import kotlin.test.assertEquals
import kotlin.test.assertNull

object RegisterViewModelTest : Spek({

    val firebaseAuthenticationRepository:
        IAuthenticationRepository by memoized { mockk(relaxed = true) }
    val registerViewModel: RegisterViewModel by memoized {
        RegisterViewModel(firebaseAuthenticationRepository)
    }

    beforeEachTest {
        SpekInstantTaskExecutorRule.execute()
    }

    afterEachTest { SpekInstantTaskExecutorRule.finish() }

    describe("authResult value") {
        it("init value is null") {
            assertNull(registerViewModel.authResult.value)
        }
        it("register success") {
            runBlocking {
                coEvery {
                    firebaseAuthenticationRepository
                        .register("", "")
                } returns Result.Success("111")
                registerViewModel.register("", "")
                assertEquals(AuthUiState.Success, registerViewModel.authResult.value)
            }
        }
        it("register error") {
            runBlocking {
                coEvery {
                    firebaseAuthenticationRepository
                        .register("", "")
                } returns Result.Error(Exception())
                registerViewModel.register("", "")
                assertEquals(AuthUiState.Error, registerViewModel.authResult.value)
            }
        }
    }
})

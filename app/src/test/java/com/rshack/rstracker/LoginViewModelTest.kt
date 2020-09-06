package com.rshack.rstracker

import com.rshack.rstracker.model.repository.IAuthenticationRepository
import com.rshack.rstracker.utils.AuthUiState
import com.rshack.rstracker.utils.Result
import com.rshack.rstracker.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.Exception
import kotlin.test.assertEquals
import kotlin.test.assertNull

object LoginViewModelTest : Spek({

    val firebaseAuthenticationRepository:
        IAuthenticationRepository by memoized { mockk(relaxed = true) }
    val loginViewModel: LoginViewModel by memoized {
        LoginViewModel(firebaseAuthenticationRepository)
    }

    beforeEachTest {
        SpekInstantTaskExecutorRule.execute()
    }

    afterEachTest { SpekInstantTaskExecutorRule.finish() }

    describe("authResult value") {
        it("init value is null") {
            assertNull(loginViewModel.authResult.value)
        }
        it("login success") {
            runBlocking {
                coEvery {
                    firebaseAuthenticationRepository
                        .login("", "")
                } returns Result.Success("111")
                loginViewModel.login("", "")
                assertEquals(AuthUiState.Success, loginViewModel.authResult.value)
            }
        }
        it("login error and clear") {
            runBlocking {
                coEvery {
                    firebaseAuthenticationRepository
                        .login("", "")
                } returns Result.Error(Exception())
                loginViewModel.login("", "")
                assertEquals(AuthUiState.Error, loginViewModel.authResult.value)
                loginViewModel.clearAuthResult()
                assertNull(loginViewModel.authResult.value)
            }
        }
        it("verify getFirebaseAuth") {
            loginViewModel.getFirebaseAuth()
            verify {
                firebaseAuthenticationRepository.getAuth()
            }
        }
    }
})

package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.AuthUserEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ConfirmEmailEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.RequestWithEmailEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class VerifyEmailViewModel(
    private val email: String,
    private val password: String,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(VerifyEmailViewState())
    val viewState: StateFlow<VerifyEmailViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<VerifyEmailUiState>(VerifyEmailUiState.Idle)
    val uiState: State<VerifyEmailUiState>
        get() = _uiState

    fun handleEvent(event: VerifyEmailEvent) {
        when (event) {
            is VerifyEmailEvent.VerifyEmail -> verifyEmail(
                event.otp,
                event.otpInputFilled
            )

            VerifyEmailEvent.ResetUiState -> _uiState.value = VerifyEmailUiState.Idle
            VerifyEmailEvent.ResendCode -> resendCode()
        }
    }

    private fun verifyEmail(otp: String, isLastDigit: Boolean) {
        _viewState.update {
            it.copy(
                otp = otp
            )
        }
        if (isLastDigit) {
            if (password.isNotEmpty())
                confirmUser()
            else
                _uiState.value = VerifyEmailUiState.OnRestore
        }
    }

    private fun confirmUser() {
        networkExecutor {
            execute {
                authRepository.confirmEmail(
                    ConfirmEmailEntity(
                        email = email,
                        code = _viewState.value.otp
                    )
                )
            }
            onSuccess {
                loginUser()
            }
            onError { error ->
                _uiState.value = VerifyEmailUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun loginUser() {
        networkExecutor {
            execute {
                authRepository.loginUser(
                    AuthUserEntity(
                        email = email,
                        password = password
                    )
                )
            }
            onSuccess {
                _uiState.value = VerifyEmailUiState.OnLogin
            }
            onError { error ->
                _uiState.value = VerifyEmailUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun resendCode() {
        networkExecutor {
            execute {
                authRepository.resendCode(
                    RequestWithEmailEntity(
                        email = email
                    )
                )
            }
            onError { error ->
                _uiState.value = VerifyEmailUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}
package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.LoginRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class VerifyEmailViewModel(
    private val email: String,
    private val password: String,
    private val navigator: ScreenNavigator,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(VerifyEmailViewState())
    val viewState: StateFlow<VerifyEmailViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    fun handleEvent(event: VerifyEmailEvent) {
        when (event) {
            is VerifyEmailEvent.VerifyEmail -> verifyEmail(
                event.otp,
                event.otpInputFilled
            )

            VerifyEmailEvent.ResetUiState -> _uiState.value = RequestState.Idle
            VerifyEmailEvent.ResendCode -> resendCode()
            VerifyEmailEvent.OnBack -> if (password.isNotEmpty()) navigator.goBack()
            else navigator.navigate(LoginRoutes.RestorePassword)
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
                navigator.navigate(LoginRoutes.NewPassword.getDestination(email, otp))
        }
    }

    private fun confirmUser() {
        networkExecutor {
            execute {
                authRepository.confirmEmail(email, _viewState.value.otp)
            }
            onSuccess {
                loginUser()
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
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
                authRepository.loginUser(email, password)
            }
            onSuccess {
                navigator.navigate(Graphs.UserProfile)
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
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
                authRepository.resendCode(email)
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
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
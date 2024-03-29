package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.LoginRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onErrorWithCode
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onStart
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.net.HttpURLConnection

class VerifyEmailViewModel(
    private val communicator: VerifyEmailCommunicator,
    private val navigator: ScreenNavigator,
    private val authRepository: AuthRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(VerifyEmailViewState())
    val viewState: StateFlow<VerifyEmailViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<RequestState>(RequestState.Idle)
    val uiState: StateFlow<RequestState>
        get() = _uiState.asStateFlow()

    fun handleEvent(event: VerifyEmailEvent) {
        when (event) {
            is VerifyEmailEvent.Verify -> verifyEmail(
                event.otp,
                event.otpInputFilled
            )

            VerifyEmailEvent.ResetUiState -> _uiState.update { RequestState.Idle }
            VerifyEmailEvent.ResendCode -> resendCode()
            VerifyEmailEvent.OnBack -> if (communicator.isForPassword.value) navigator.goBack()
            else navigator.navigate(LoginRoutes.RestorePassword)

            else -> Unit
        }
    }

    private fun verifyEmail(otp: String, isLastDigit: Boolean) {
        _viewState.update {
            it.copy(
                otp = otp
            )
        }
        if (isLastDigit) {
            if (communicator.isForPassword.value)
                resetPassword()
            else
                confirmUser()
        }
    }

    private fun confirmUser() {
        val otp = _viewState.value.otp
        networkExecutor {
            onStart {
                _viewState.update {
                    it.copy(otp = "")
                }
            }
            execute {
                authRepository.confirmEmail(communicator.email.value, otp)
            }
            onSuccess {
                _viewState.update {
                    it.copy(otp = "")
                }

                loginUser()
            }
            onErrorWithCode { error, code ->
                if (code == HttpURLConnection.HTTP_BAD_REQUEST)
                    _viewState.update {
                        it.copy(otpValid = ValidField.INVALID)
                    }
                else
                    _uiState.update { RequestState.OnError(error) }
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

    private fun resetPassword() {
        val otp = _viewState.value.otp
        networkExecutor {
            onStart {
                _viewState.update {
                    it.copy(otp = "")
                }
            }
            execute {
                authRepository.resetPassword(communicator.email.value, otp, communicator.password.value)
            }
            onSuccess {
                navigator.navigate(LoginRoutes.RestoreSuccess)
            }
            onErrorWithCode { error, code ->
                if (code == HttpURLConnection.HTTP_BAD_REQUEST)
                    _viewState.update {
                        it.copy(otpValid = ValidField.INVALID)
                    }
                else
                    _uiState.update { RequestState.OnError(error) }
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
                authRepository.loginUser(communicator.email.value, communicator.password.value)
            }
            onSuccess {
                viewModelScope.launch {
                    val token = Firebase.messaging.token.await()
                    if (token.isNotEmpty())
                        preferencesDatastoreManager.fcmToken = token

                    navigator.navigate(Graphs.UserProfile)
                }
            }
            onError { error ->
                _uiState.update { RequestState.OnError(error) }
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
                if (communicator.isForPassword.value) authRepository.forgetPassword(communicator.email.value)
                else authRepository.resendCode(communicator.email.value)
            }
            onError { error ->
                _uiState.update { RequestState.OnError(error) }
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(isLoading = isLoading )
                }
            }
        }
    }
}
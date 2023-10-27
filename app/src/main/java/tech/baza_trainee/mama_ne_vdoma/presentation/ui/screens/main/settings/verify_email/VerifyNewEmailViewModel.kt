package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.verify_email

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.common.EditProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onErrorWithCode
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onStart
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.net.HttpURLConnection

class VerifyNewEmailViewModel(
    private val communicator: EditProfileCommunicator,
    private val navigator: ScreenNavigator,
    private val authRepository: AuthRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(VerifyEmailViewState())
    val viewState: StateFlow<VerifyEmailViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<VerifyEmailUiState>(VerifyEmailUiState.Idle)
    val uiState: State<VerifyEmailUiState>
        get() = _uiState

    fun handleEvent(event: VerifyEmailEvent) {
        when (event) {
            is VerifyEmailEvent.Verify -> verify(
                event.otp,
                event.otpInputFilled
            )

            VerifyEmailEvent.ResetUiState -> _uiState.value = VerifyEmailUiState.Idle
            VerifyEmailEvent.ResendCode -> resendCode()
            VerifyEmailEvent.OnBack -> navigator.goBack()
            VerifyEmailEvent.GoToMain -> navigator.navigate(Graphs.Login)
        }
    }

    private fun verify(otp: String, isLastDigit: Boolean) {
        _viewState.update {
            it.copy(
                otp = otp
            )
        }
        if (isLastDigit) {
            if (communicator.isForReset.value)
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
                authRepository.changeEmail(otp)
            }
            onSuccess {
                _uiState.value = VerifyEmailUiState.OnEmailChanged
            }
            onErrorWithCode { error, code ->
                if (code == HttpURLConnection.HTTP_BAD_REQUEST)
                    _viewState.update {
                        it.copy(otpValid = ValidField.INVALID)
                    }
                else
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

    private fun resetPassword() {
        val otp = _viewState.value.otp
        networkExecutor {
            onStart {
                _viewState.update {
                    it.copy(otp = "")
                }
            }
            execute {
                authRepository.resetPassword(preferencesDatastoreManager.email, otp, communicator.password.value)
            }
            onSuccess {
                _uiState.value = VerifyEmailUiState.OnPasswordChanged
            }
            onErrorWithCode { error, code ->
                if (code == HttpURLConnection.HTTP_BAD_REQUEST)
                    _viewState.update {
                        it.copy(otpValid = ValidField.INVALID)
                    }
                else
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
                authRepository.changeEmailInit(communicator.email.value)
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
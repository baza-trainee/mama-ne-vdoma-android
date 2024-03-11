package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.LoginRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class RestorePasswordScreenViewModel(
    private val communicator: VerifyEmailCommunicator,
    private val navigator: ScreenNavigator,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(RestorePasswordViewState())
    val viewState: StateFlow<RestorePasswordViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<RequestState>(RequestState.Idle)
    val uiState: StateFlow<RequestState>
        get() = _uiState.asStateFlow()

    fun handleRestoreEvent(event: RestorePasswordEvent) {
        when(event) {
            RestorePasswordEvent.OnBack -> navigator.navigate(LoginRoutes.Login)
            RestorePasswordEvent.SendEmail -> forgetPassword()
            RestorePasswordEvent.ResetUiState -> _uiState.update { RequestState.Idle }

            is RestorePasswordEvent.ValidateEmail -> validateEmail(event.email)
            is RestorePasswordEvent.OnLogin -> {
                communicator.apply {
                    setEmail(event.email)
                    setPassword(event.password)
                    setForPassword(true)
                }
                navigator.navigate(LoginRoutes.VerifyEmail)
            }
        }
    }


    private fun validateEmail(email: String) {
        val emailValid = if (email.validateEmail()) ValidField.VALID
        else ValidField.INVALID
        _viewState.update {
            it.copy(
                email = email,
                emailValid = emailValid
            )
        }
    }

    private fun forgetPassword() {
        networkExecutor {
            execute {
                authRepository.forgetPassword(_viewState.value.email)
            }
            onSuccess {
                communicator.setEmail(_viewState.value.email)
                navigator.navigate(LoginRoutes.NewPassword)
            }
            onError { error ->
                _uiState.update { RequestState.OnError(error) }
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(isLoading = isLoading)
                }
            }
        }
    }
}
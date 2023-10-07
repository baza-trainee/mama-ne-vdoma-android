package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.AuthUserEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.CommonUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validatePassword
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class LoginScreenViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<CommonUiState>(CommonUiState.Idle)
    val uiState: State<CommonUiState>
        get() = _uiState

    fun handleLoginEvent(event: LoginEvent) {
        when(event) {
            LoginEvent.LoginUser -> loginUser()
            is LoginEvent.ValidateEmail -> validateEmail(event.email)
            is LoginEvent.ValidatePassword -> validatePassword(event.password)
            LoginEvent.OnSuccessfulLogin -> {
                _viewState.update {
                    LoginViewState()
                }
            }

            LoginEvent.ResetUiState -> _uiState.value = CommonUiState.Idle
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

    private fun validatePassword(password: String) {
        val passwordValid = if (password.validatePassword()) ValidField.VALID
        else ValidField.INVALID
        _viewState.update {
            it.copy(
                password = password,
                passwordValid = passwordValid
            )
        }
    }

    private fun loginUser() {
        networkExecutor {
            execute {
                authRepository.loginUser(
                    AuthUserEntity(
                        email = _viewState.value.email,
                        password = _viewState.value.password
                    )
                )
            }
            onSuccess {
                _uiState.value = CommonUiState.OnNext
            }
            onError { error ->
                _uiState.value = CommonUiState.OnError(error)
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
package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login

import androidx.lifecycle.ViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.AuthUserEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
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

    fun handleLoginEvent(event: LoginEvent) {
        when(event) {
            LoginEvent.ConsumeRequestError -> consumeRequestError()
            LoginEvent.LoginUser -> loginUser()
            is LoginEvent.ValidateEmail -> validateEmail(event.email)
            is LoginEvent.ValidatePassword -> validatePassword(event.password)
            LoginEvent.OnSuccessfulLogin -> {
                _viewState.update {
                    LoginViewState()
                }
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
                _viewState.update {
                    it.copy(
                        loginSuccess = triggered
                    )
                }
            }
            onError { error ->
                _viewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
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

    private fun consumeRequestError() {
        _viewState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }
}
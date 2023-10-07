package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

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

class UserCreateViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(UserCreateViewState())
    val viewState: StateFlow<UserCreateViewState> = _viewState.asStateFlow()

    fun handleUserCreateEvent(event: UserCreateEvent) {
        when(event) {
            UserCreateEvent.ConsumeRequestError -> consumeRequestError()
            UserCreateEvent.ConsumeRequestSuccess -> consumeRequestSuccess()
            UserCreateEvent.RegisterUser -> registerUser()
            is UserCreateEvent.UpdatePolicyCheck -> updatePolicyCheck(event.isChecked)
            is UserCreateEvent.ValidateConfirmPassword -> validateConfirmPassword(event.confirmPassword)
            is UserCreateEvent.ValidateEmail -> validateEmail(event.email)
            is UserCreateEvent.ValidatePassword -> validatePassword(event.password)
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
        updateConform()
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
        checkPasswords(password, _viewState.value.confirmPassword)
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        _viewState.update {
            it.copy(
                confirmPassword = confirmPassword
            )
        }
        checkPasswords(_viewState.value.password, confirmPassword)
        updateConform()
    }

    private fun updatePolicyCheck(isChecked: Boolean) {
        _viewState.update {
            it.copy(
                isPolicyChecked = isChecked
            )
        }
        updateConform()
    }

    private fun checkPasswords(password: String, confirmPassword: String) {
        val confirmPasswordValid = if (password == confirmPassword) ValidField.VALID
        else ValidField.INVALID
        _viewState.update {
            it.copy(
                confirmPasswordValid = confirmPasswordValid
            )
        }
        updateConform()
    }

    private fun updateConform() {
        _viewState.update {
            it.copy(
                isAllConform =
                it.emailValid == ValidField.VALID && it.passwordValid == ValidField.VALID &&
                        it.confirmPasswordValid == ValidField.VALID && it.isPolicyChecked
            )
        }
    }

    private fun registerUser() {
        networkExecutor {
            execute {
                authRepository.registerUser(
                    AuthUserEntity(
                        email = _viewState.value.email,
                        password = _viewState.value.password
                    )
                )
            }
            onSuccess {
                _viewState.update {
                    it.copy(
                        registerSuccess = triggered
                    )
                }
            }
            onError { error ->
                _viewState.update {
                    it.copy(
                        registerError = triggered(error)
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
                registerError = consumed()
            )
        }
    }

    private fun consumeRequestSuccess() {
        _viewState.update {
            it.copy(
                registerSuccess = consumed
            )
        }
    }
}
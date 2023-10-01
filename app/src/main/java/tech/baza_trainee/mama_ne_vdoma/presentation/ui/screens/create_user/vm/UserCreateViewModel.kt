package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm

import androidx.lifecycle.ViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.AuthUserEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ConfirmEmailEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserCreateViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.VerifyEmailViewState
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

    private val _userCreateViewState = MutableStateFlow(UserCreateViewState())
    val userCreateViewState: StateFlow<UserCreateViewState> = _userCreateViewState.asStateFlow()

    private val _verifyEmailViewState = MutableStateFlow(VerifyEmailViewState())
    val verifyEmailViewState: StateFlow<VerifyEmailViewState> = _verifyEmailViewState.asStateFlow()

    fun validateEmail(email: String) {
        val emailValid = if (email.validateEmail()) ValidField.VALID
        else ValidField.INVALID
        _userCreateViewState.update {
            it.copy(
                email = email,
                emailValid = emailValid
            )
        }
        updateConform()
    }

    fun validatePassword(password: String) {
        val passwordValid = if (password.validatePassword()) ValidField.VALID
        else ValidField.INVALID
        _userCreateViewState.update {
            it.copy(
                password = password,
                passwordValid = passwordValid
            )
        }
        checkPasswords(password, _userCreateViewState.value.confirmPassword)
    }

    fun validateConfirmPassword(confirmPassword: String) {
        checkPasswords(_userCreateViewState.value.password, confirmPassword)
        _userCreateViewState.update {
            it.copy(
                confirmPassword = confirmPassword
            )
        }
        updateConform()
    }

    fun updatePolicyCheck(isChecked: Boolean) {
        _userCreateViewState.update {
            it.copy(
                isPolicyChecked = isChecked
            )
        }
        updateConform()
    }

    private fun checkPasswords(password: String, confirmPassword: String) {
        val confirmPasswordValid = if (password == confirmPassword) ValidField.VALID
        else ValidField.INVALID
        _userCreateViewState.update {
            it.copy(
                confirmPasswordValid = confirmPasswordValid
            )
        }
        updateConform()
    }

    private fun updateConform() {
        _userCreateViewState.update {
            it.copy(
                isAllConform =
                it.emailValid == ValidField.VALID && it.passwordValid == ValidField.VALID &&
                        it.confirmPasswordValid == ValidField.VALID && it.isPolicyChecked
            )
        }
    }

    fun registerUser() {
        networkExecutor {
            execute {
                authRepository.registerUser(
                    AuthUserEntity(
                        email = _userCreateViewState.value.email,
                        password = _userCreateViewState.value.password
                    )
                )
            }
            onSuccess {
                _userCreateViewState.update {
                    it.copy(
                        registerSuccess = triggered
                    )
                }
            }
            onError { error ->
                _userCreateViewState.update {
                    it.copy(
                        registerError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _userCreateViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    fun consumeRegisterError() {
        _userCreateViewState.update {
            it.copy(
                registerError = consumed()
            )
        }
    }

    fun verifyEmail(otp: String, isLastDigit: Boolean, onSuccess: () -> Unit) {
        _verifyEmailViewState.update {
            it.copy(
                otp = otp
            )
        }
        if (isLastDigit) {
            onSuccess() //for test
//            confirmUser { onSuccess() }
        }
    }

    private fun confirmUser(onSuccess: () -> Unit) {
        networkExecutor {
            execute {
                authRepository.confirmEmail(
                    ConfirmEmailEntity(
                        email = _userCreateViewState.value.email,
                        code = _verifyEmailViewState.value.otp
                    )
                )
            }
            onSuccess {  }
            onError {  }
            onLoading { isLoading ->
                _verifyEmailViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}
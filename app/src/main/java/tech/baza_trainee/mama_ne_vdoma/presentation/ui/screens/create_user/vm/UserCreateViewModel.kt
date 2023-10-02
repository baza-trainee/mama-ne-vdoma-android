package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.AuthUserEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ConfirmEmailEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ResendCodeEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserCreateEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserCreateViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.VerifyEmailEvent
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

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var otp by mutableStateOf("")
        private set

    fun handleUserCreateEvent(event: UserCreateEvent) {
        when(event) {
            UserCreateEvent.ConsumeRegisterError -> consumeRegisterError()
            UserCreateEvent.RegisterUser -> registerUser()
            is UserCreateEvent.UpdatePolicyCheck -> updatePolicyCheck(event.isChecked)
            is UserCreateEvent.ValidateConfirmPassword -> validateConfirmPassword(event.confirmPassword)
            is UserCreateEvent.ValidateEmail -> validateEmail(event.email)
            is UserCreateEvent.ValidatePassword -> validatePassword(event.password)
        }
    }

    fun handleVerifyEmailEvent(event: VerifyEmailEvent) {
        when(event) {
            is VerifyEmailEvent.VerifyEmail -> verifyEmail(
                event.otp,
                event.otpInputFilled,
                event.onSuccess
            )

            VerifyEmailEvent.ConsumeRequestError -> consumeRequestError()
            VerifyEmailEvent.ResendCode -> resendCode()
        }
    }

    private fun validateEmail(email: String) {
        this.email = email
        val emailValid = if (email.validateEmail()) ValidField.VALID
        else ValidField.INVALID
        _userCreateViewState.update {
            it.copy(
                emailValid = emailValid
            )
        }
        updateConform()
    }

    private fun validatePassword(password: String) {
        this.password = password
        val passwordValid = if (password.validatePassword()) ValidField.VALID
        else ValidField.INVALID
        _userCreateViewState.update {
            it.copy(
                passwordValid = passwordValid
            )
        }
        checkPasswords(password, confirmPassword)
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        this.confirmPassword = confirmPassword
        checkPasswords(password, confirmPassword)
        updateConform()
    }

    private fun updatePolicyCheck(isChecked: Boolean) {
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

    private fun registerUser() {
        networkExecutor {
            execute {
                authRepository.registerUser(
                    AuthUserEntity(
                        email = email,
                        password = password
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

    private fun consumeRegisterError() {
        _userCreateViewState.update {
            it.copy(
                registerError = consumed()
            )
        }
    }

    private fun verifyEmail(otp: String, isLastDigit: Boolean, onSuccess: () -> Unit) {
        this.otp = otp
        if (isLastDigit) {
//            onSuccess() //for test
            confirmUser { onSuccess() }
        }
    }

    private fun confirmUser(onSuccess: () -> Unit) {
        networkExecutor {
            execute {
                authRepository.confirmEmail(
                    ConfirmEmailEntity(
                        email = email,
                        code = otp
                    )
                )
            }
            onSuccess { loginUser() }
            onError { error ->
                _verifyEmailViewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _verifyEmailViewState.update {
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
                _verifyEmailViewState.update {
                    it.copy(
                        loginSuccess = triggered
                    )
                }
            }
            onError { error ->
                _verifyEmailViewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _verifyEmailViewState.update {
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
                    ResendCodeEntity(
                        email = email
                    )
                )
            }
            onError { error ->
                _verifyEmailViewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _verifyEmailViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun consumeRequestError() {
        _verifyEmailViewState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }
}
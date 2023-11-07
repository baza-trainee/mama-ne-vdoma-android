package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.change_credentials

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserAuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.common.EditProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validatePassword
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class EditCredentialsViewModel(
    private val navigator: PageNavigator,
    private val authRepository: AuthRepository,
    private val userAuthRepository: UserAuthRepository,
    private val profileCommunicator: EditProfileCommunicator,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(EditCredentialsViewState())
    val viewState: StateFlow<EditCredentialsViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    fun handleEvent(event: EditCredentialsEvent) {
        when (event) {
            EditCredentialsEvent.OnBack -> navigator.goToPrevious()
            EditCredentialsEvent.ResetPassword -> forgetPassword()
            EditCredentialsEvent.ResetUiState -> _uiState.value = RequestState.Idle
            is EditCredentialsEvent.ValidateConfirmPassword -> validateConfirmPassword(event.confirmPassword)
            is EditCredentialsEvent.ValidateEmail -> validateEmail(event.email)
            is EditCredentialsEvent.ValidatePassword -> validatePassword(event.password)
            EditCredentialsEvent.VerifyEmail -> verifyEmail()
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
        checkPasswords(password, _viewState.value.confirmPassword)
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        _viewState.update {
            it.copy(
                confirmPassword = confirmPassword
            )
        }
        checkPasswords(_viewState.value.password, confirmPassword)
    }

    private fun checkPasswords(password: String, confirmPassword: String) {
        val confirmPasswordValid = if (password == confirmPassword) ValidField.VALID
        else ValidField.INVALID
        _viewState.update {
            it.copy(
                confirmPasswordValid = confirmPasswordValid
            )
        }
    }

    private fun verifyEmail() {
        networkExecutor {
            execute {
                userAuthRepository.changeEmailInit(_viewState.value.email)
            }
            onSuccess {
                profileCommunicator.apply {
                    setEmail(_viewState.value.email)
                    setForPassword(false)
                }
                navigator.navigate(SettingsScreenRoutes.VerifyNewEmail)
            }
            onError {
                _uiState.value = RequestState.OnError(it)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(isLoading = isLoading)
                }
            }
        }
    }

    private fun forgetPassword() {
        networkExecutor {
            execute {
                authRepository.forgetPassword(preferencesDatastoreManager.email)
            }
            onSuccess {
                profileCommunicator.apply {
                    setPassword(_viewState.value.password)
                    setForPassword(true)
                }
                navigator.navigate(SettingsScreenRoutes.VerifyNewEmail)
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
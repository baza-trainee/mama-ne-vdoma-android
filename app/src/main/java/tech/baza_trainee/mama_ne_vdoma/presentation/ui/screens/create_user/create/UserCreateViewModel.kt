package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.create

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CreateUserRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validatePassword
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class UserCreateViewModel(
    private val navigator: ScreenNavigator,
    private val authRepository: AuthRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val communicator: VerifyEmailCommunicator
): ViewModel() {

    private val _viewState = MutableStateFlow(UserCreateViewState())
    val viewState: StateFlow<UserCreateViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<RequestState>(RequestState.Idle)
    val uiState: StateFlow<RequestState>
        get() = _uiState.asStateFlow()

    fun handleUserCreateEvent(event: UserCreateEvent) {
        when(event) {
            UserCreateEvent.RegisterUser -> registerUser()
            UserCreateEvent.ResetUiState -> _uiState.update { RequestState.Idle }
            is UserCreateEvent.UpdatePolicyCheck -> updatePolicyCheck(event.isChecked)
            is UserCreateEvent.ValidateConfirmPassword -> validateConfirmPassword(event.confirmPassword)
            is UserCreateEvent.ValidateEmail -> validateEmail(event.email)
            is UserCreateEvent.ValidatePassword -> validatePassword(event.password)
            UserCreateEvent.OnBack -> navigator.navigate(Graphs.Start)
            UserCreateEvent.OnLogin -> navigator.navigate(Graphs.Login)
            is UserCreateEvent.OnGoogleLogin -> signupWithGoogle(event.token)
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
                authRepository.registerUser(_viewState.value.email, _viewState.value.password)
            }
            onSuccess {
                communicator.apply {
                    setEmail(_viewState.value.email)
                    setPassword(_viewState.value.password)
                    setForPassword(false)
                }
                navigator.navigate(CreateUserRoute.VerifyEmail)
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

    private fun signupWithGoogle(token: String) {
        networkExecutor {
            execute {
                authRepository.signupWithGoogle(token)
            }
            onSuccess {
                if (preferencesDatastoreManager.id != it) {
                    val cookies = preferencesDatastoreManager.cookies
                    preferencesDatastoreManager.clearData()
                    preferencesDatastoreManager.cookies = cookies
                }
                navigator.navigate(Graphs.UserProfile)
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
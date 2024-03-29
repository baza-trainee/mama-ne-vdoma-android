package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login

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
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.LoginRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.EDIT_PROFILE_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.MAIN_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validatePassword
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class LoginViewModel(
    private val navigator: ScreenNavigator,
    private val authRepository: AuthRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<RequestState>(RequestState.Idle)
    val uiState: StateFlow<RequestState>
        get() = _uiState.asStateFlow()

    fun handleLoginEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.LoginUser -> loginWithPassword(
                _viewState.value.email,
                _viewState.value.password
            )

            LoginEvent.ResetUiState -> _uiState.update { RequestState.Idle }
            LoginEvent.OnBack -> {
                clearInputs()
                navigator.navigate(Graphs.Start)
            }

            LoginEvent.OnCreate -> {
                clearInputs()
                navigator.navigate(Graphs.CreateUser)
            }

            LoginEvent.OnRestore -> {
                clearInputs()
                navigator.navigate(LoginRoutes.RestorePassword)
            }

            is LoginEvent.ValidateEmail -> validateEmail(event.email)
            is LoginEvent.ValidatePassword -> validatePassword(event.password)
            is LoginEvent.LoginWithPassword -> loginWithPassword(event.email, event.password)
            is LoginEvent.LoginWithToken -> signInWithGoogle(event.token)
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

    private fun loginWithPassword(email: String, password: String) {
        networkExecutor {
            execute {
                authRepository.loginUser(email, password)
            }
            onSuccess {
                clearInputs()
                checkUser(it)
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

    private fun clearInputs() {
        _viewState.update {
            LoginViewState()
        }
    }

    private fun signInWithGoogle(token: String) {
        networkExecutor {
            execute {
                authRepository.signupWithGoogle(token)
            }
            onSuccess { checkUser(it) }
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

    private fun checkUser(id: String) {
        viewModelScope.launch {
            val token = Firebase.messaging.token.await()
            if (token.isNotEmpty())
                preferencesDatastoreManager.fcmToken = token

            if (id == preferencesDatastoreManager.id)
                navigator.navigate(HostScreenRoutes.Host.getDestination(MAIN_PAGE))
            else {
                val cookies = preferencesDatastoreManager.cookies
                preferencesDatastoreManager.clearData()
                preferencesDatastoreManager.cookies = cookies
                navigator.navigate(HostScreenRoutes.Host.getDestination(EDIT_PROFILE_PAGE))
            }
        }
    }
}
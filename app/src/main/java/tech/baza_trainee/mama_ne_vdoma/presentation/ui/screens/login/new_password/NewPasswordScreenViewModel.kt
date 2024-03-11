package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.LoginRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validatePassword

class NewPasswordScreenViewModel(
    private val communicator: VerifyEmailCommunicator,
    private val navigator: ScreenNavigator
): ViewModel() {

    private val _viewState = MutableStateFlow(NewPasswordViewState())
    val viewState: StateFlow<NewPasswordViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<RequestState>(RequestState.Idle)
    val uiState: StateFlow<RequestState>
        get() = _uiState.asStateFlow()


    fun handleNewPasswordEvent(event: NewPasswordEvent) {
        when(event) {
            NewPasswordEvent.OnBack -> navigator.navigate(LoginRoutes.Login)
            NewPasswordEvent.ResetPassword -> navigator.navigate(LoginRoutes.EmailConfirm.getDestination(communicator.email.value, _viewState.value.password))
            is NewPasswordEvent.ValidatePassword -> validatePassword(event.password)
            is NewPasswordEvent.ValidateConfirmPassword -> validateConfirmPassword(event.confirmPassword)
            NewPasswordEvent.ResetUiState -> _uiState.update { RequestState.Idle }
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
}
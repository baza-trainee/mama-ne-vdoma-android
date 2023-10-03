package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm

import androidx.lifecycle.ViewModel
import de.palm.composestateevents.consumed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.model.NewPasswordEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.model.NewPasswordViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validatePassword

class NewPasswordScreenViewModel: ViewModel() {

    private val _viewState = MutableStateFlow(NewPasswordViewState())
    val viewState: StateFlow<NewPasswordViewState> = _viewState.asStateFlow()

    fun handleNewPasswordEvent(event: NewPasswordEvent) {
        when(event) {
            NewPasswordEvent.ConsumeRequestError -> consumeRegisterError()
            NewPasswordEvent.OnSuccess -> Unit //todo
            is NewPasswordEvent.ValidatePassword -> validatePassword(event.password)
            is NewPasswordEvent.ValidateConfirmPassword -> validateConfirmPassword(event.confirmPassword)
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

    private fun consumeRegisterError() {
        _viewState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }
}
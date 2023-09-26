package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserCreateViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.validateEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.validatePassword

class UserCreateViewModel: ViewModel() {

    private val _viewState = MutableStateFlow(UserCreateViewState())
    val viewState: StateFlow<UserCreateViewState> = _viewState.asStateFlow()

    fun validateEmail(email: String) {
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

    fun validatePassword(password: String) {
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

    fun validateConfirmPassword(confirmPassword: String) {
        checkPasswords(_viewState.value.password, confirmPassword)
        _viewState.update {
            it.copy(
                confirmPassword = confirmPassword
            )
        }
        updateConform()
    }

    fun updatePolicyCheck(isChecked: Boolean) {
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
}
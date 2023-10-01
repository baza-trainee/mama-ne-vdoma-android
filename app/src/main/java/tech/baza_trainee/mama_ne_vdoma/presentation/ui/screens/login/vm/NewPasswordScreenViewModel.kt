package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.model.NewPasswordViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validatePassword

class NewPasswordScreenViewModel: ViewModel() {

    private val _viewState = MutableStateFlow(NewPasswordViewState())
    val viewState: StateFlow<NewPasswordViewState> = _viewState.asStateFlow()

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    fun validatePassword(password: String) {
        this.password = password
        val passwordValid = if (password.validatePassword()) ValidField.VALID
        else ValidField.INVALID
        _viewState.update {
            it.copy(
                passwordValid = passwordValid
            )
        }
        checkPasswords(password, confirmPassword)
    }

    fun validateConfirmPassword(confirmPassword: String) {
        this.confirmPassword = confirmPassword
        checkPasswords(password, confirmPassword)
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
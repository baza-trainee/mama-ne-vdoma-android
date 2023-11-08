package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class VerifyEmailCommunicator {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isForPassword = MutableStateFlow(false)
    val isForPassword: StateFlow<Boolean> = _isForPassword.asStateFlow()

    fun setEmail(value: String) {
        _email.update {
            value
        }
    }

    fun setPassword(value: String) {
        _password.update {
            value
        }
    }

    fun setForPassword(value: Boolean) {
        _isForPassword.update {
            value
        }
    }
}
package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class VerifyEmailCommunicator {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _emailChanged = MutableStateFlow(false)
    val emailChanged: StateFlow<Boolean> = _emailChanged.asStateFlow()

    fun setEmail(value: String) {
        _email.update {
            value
        }
    }

    fun setEmailChanged(value: Boolean) {
        _emailChanged.update {
            value
        }
    }
}
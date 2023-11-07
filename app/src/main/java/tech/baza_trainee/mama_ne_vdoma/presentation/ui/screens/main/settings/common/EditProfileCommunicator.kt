package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class EditProfileCommunicator {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isForPassword = MutableStateFlow(false)
    val isForPassword: StateFlow<Boolean> = _isForPassword.asStateFlow()

    private val _profileChanged = MutableStateFlow(false)
    val profileChanged: StateFlow<Boolean> = _profileChanged.asStateFlow()

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

    fun setProfileChanged(value: Boolean) {
        _profileChanged.update {
            value
        }
    }
}
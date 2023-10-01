package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.model.RestorePasswordViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateEmail

class RestorePasswordScreenViewModel: ViewModel() {

    private val _viewState = MutableStateFlow(RestorePasswordViewState())
    val viewState: StateFlow<RestorePasswordViewState> = _viewState.asStateFlow()

    var email by mutableStateOf("")
        private set

    fun validateEmail(email: String) {
        this.email = email
        val emailValid = if (email.validateEmail()) ValidField.VALID
        else ValidField.INVALID
        _viewState.update {
            it.copy(
                emailValid = emailValid
            )
        }
    }
}
package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.RestorePasswordEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.CommonUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validatePassword
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class NewPasswordScreenViewModel(
    private val email: String,
    private val otp: String,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(NewPasswordViewState())
    val viewState: StateFlow<NewPasswordViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<CommonUiState>(CommonUiState.Idle)
    val uiState: State<CommonUiState>
        get() = _uiState


    fun handleNewPasswordEvent(event: NewPasswordEvent) {
        when(event) {
            NewPasswordEvent.ResetPassword -> resetPassword()
            is NewPasswordEvent.ValidatePassword -> validatePassword(event.password)
            is NewPasswordEvent.ValidateConfirmPassword -> validateConfirmPassword(event.confirmPassword)
            NewPasswordEvent.ResetUiState -> _uiState.value = CommonUiState.Idle
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

    private fun resetPassword() {
        networkExecutor {
            execute {
                authRepository.resetPassword(
                    RestorePasswordEntity(
                        email = email,
                        password = _viewState.value.password,
                        code = otp
                    )
                )
            }
            onSuccess {
                _uiState.value = CommonUiState.OnNext
            }
            onError { error ->
                _uiState.value = CommonUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}
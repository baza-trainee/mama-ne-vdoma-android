package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password

import androidx.lifecycle.ViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.RequestWithEmailEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class RestorePasswordScreenViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(RestorePasswordViewState())
    val viewState: StateFlow<RestorePasswordViewState> = _viewState.asStateFlow()

    fun handleRestoreEvent(event: RestorePasswordEvent) {
        when(event) {
            RestorePasswordEvent.ConsumeRequestError -> consumeRequestError()
            RestorePasswordEvent.SendEmail -> forgetPassword()

            RestorePasswordEvent.OnSuccess -> _viewState.update {
                RestorePasswordViewState()
            }

            is RestorePasswordEvent.ValidateEmail -> validateEmail(event.email)
            RestorePasswordEvent.ConsumeRequestSuccess -> consumeRegisterSuccess()
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

    private fun forgetPassword() {
        networkExecutor {
            execute {
                authRepository.forgetPassword(
                    RequestWithEmailEntity(
                        email = _viewState.value.email
                    )
                )
            }
            onSuccess {
                _viewState.update {
                    it.copy(
                        requestSuccess = triggered
                    )
                }
            }
            onError { error ->
                _viewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
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

    private fun consumeRequestError() {
        _viewState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }

    private fun consumeRegisterSuccess() {
        _viewState.update {
            it.copy(
                requestSuccess = consumed
            )
        }
    }
}
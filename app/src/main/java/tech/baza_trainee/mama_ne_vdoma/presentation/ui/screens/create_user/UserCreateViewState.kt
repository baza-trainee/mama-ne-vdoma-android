package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class UserCreateViewState(
    val email: String = "",
    val emailValid: ValidField = ValidField.EMPTY,
    val password: String = "",
    val passwordValid: ValidField = ValidField.EMPTY,
    val confirmPassword: String = "",
    val confirmPasswordValid: ValidField = ValidField.EMPTY,
    val isPolicyChecked: Boolean = false,
    val isAllConform: Boolean = false,
    val isLoading: Boolean = false,
    val registerSuccess: StateEvent = consumed,
    val registerError: StateEventWithContent<String> = consumed()
)

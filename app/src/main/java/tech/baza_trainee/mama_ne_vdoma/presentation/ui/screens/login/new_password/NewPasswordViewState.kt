package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password

import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class NewPasswordViewState(
    val password: String = "",
    val passwordValid: ValidField = ValidField.EMPTY,
    val confirmPassword: String = "",
    val confirmPasswordValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false,
    val requestSuccess: StateEvent = consumed,
    val requestError: StateEventWithContent<String> = consumed()
)

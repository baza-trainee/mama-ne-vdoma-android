package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.model

import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class RestorePasswordViewState(
    val email: String = "",
    val emailValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false,
    val loginSuccess: StateEvent = consumed,
    val requestError: StateEventWithContent<String> = consumed()
)

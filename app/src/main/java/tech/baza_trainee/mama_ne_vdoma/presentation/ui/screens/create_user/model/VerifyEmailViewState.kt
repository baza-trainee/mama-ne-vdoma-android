package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model

import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

data class VerifyEmailViewState(
    val otpValid: ValidField = ValidField.EMPTY,
    val isLoading: Boolean = false,
    val loginSuccess: StateEvent = consumed,
    val requestError: StateEventWithContent<String> = consumed()
)

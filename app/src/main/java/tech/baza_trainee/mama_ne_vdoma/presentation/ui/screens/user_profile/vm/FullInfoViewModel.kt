package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.FullInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.FullProfileEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator

class FullInfoViewModel(
    private val communicator: UserProfileCommunicator
): ViewModel() {

    private val _fullInfoViewState = MutableStateFlow(FullInfoViewState())
    val fullInfoViewState: StateFlow<FullInfoViewState> = _fullInfoViewState.asStateFlow()

    fun handleFullProfileEvent(event: FullProfileEvent) {
        when(event) {
            FullProfileEvent.UpdateFullProfile -> Unit
        }
    }
}
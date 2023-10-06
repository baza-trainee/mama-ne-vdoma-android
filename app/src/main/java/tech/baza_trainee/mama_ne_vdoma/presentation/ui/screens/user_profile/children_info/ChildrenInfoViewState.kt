package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.children_info

import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity

data class ChildrenInfoViewState(
    val children: List<ChildEntity> = emptyList(),
    val isLoading: Boolean = false,
    val requestSuccess: StateEvent = consumed,
    val requestError: StateEventWithContent<String> = consumed()
)

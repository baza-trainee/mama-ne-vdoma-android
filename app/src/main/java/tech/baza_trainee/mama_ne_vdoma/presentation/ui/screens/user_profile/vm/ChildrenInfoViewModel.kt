package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.Child
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildrenInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildrenInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import java.util.UUID

class ChildrenInfoViewModel(
    private val communicator: UserProfileCommunicator
): ViewModel() {

    private val _childrenInfoViewState = MutableStateFlow(ChildrenInfoViewState())
    val childrenInfoViewState: StateFlow<ChildrenInfoViewState> = _childrenInfoViewState.asStateFlow()

    init {
        _childrenInfoViewState.update {
            it.copy(
                children = communicator.children
            )
        }
    }

    fun handleChildrenInfoEvent(event: ChildrenInfoEvent) {
        when(event) {
            is ChildrenInfoEvent.DeleteChild -> deleteChild(event.id)
            ChildrenInfoEvent.ResetChild -> resetCurrentChild()
            is ChildrenInfoEvent.SetChild -> setCurrentChild(event.id)
        }
    }

    private fun resetCurrentChild() {
        communicator.currentChild = Child()
    }

    private fun deleteChild(childId: String) {
        with(_childrenInfoViewState.value.children.toMutableList()) {
            val child = firstOrNull { it.id == childId }
            remove(child)
            _childrenInfoViewState.update {
                it.copy(
                    children = this
                )
            }
        }
    }

    private fun setCurrentChild(childId: String = "") {
        val currentChildId = when {
            childId.isNotEmpty() -> childId
            communicator.currentChild.id.isEmpty() -> UUID.randomUUID().toString()
            else -> communicator.currentChild.id
        }

        communicator.currentChild =
            _childrenInfoViewState.value.children.firstOrNull { it.id == currentChildId }
                ?: Child(id = currentChildId)
    }
}
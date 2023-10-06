package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.children_info

import androidx.lifecycle.ViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class ChildrenInfoViewModel(
    private val communicator: UserProfileCommunicator,
    private val userProfileRepository: UserProfileRepository
): ViewModel() {

    private val _childrenInfoViewState = MutableStateFlow(ChildrenInfoViewState())
    val childrenInfoViewState: StateFlow<ChildrenInfoViewState> = _childrenInfoViewState.asStateFlow()

    init {
        getChildren()
    }

    fun handleChildrenInfoEvent(event: ChildrenInfoEvent) {
        when(event) {
            is ChildrenInfoEvent.DeleteChild -> deleteChild(event.id)
            ChildrenInfoEvent.ResetChild -> resetCurrentChild()
            is ChildrenInfoEvent.SetChild -> setCurrentChild(event.id)
            ChildrenInfoEvent.ConsumeRequestError -> consumeRequestError()
        }
    }

    private fun getChildren() {
        networkExecutor {
            execute {
                userProfileRepository.getChildren()
            }
            onSuccess { entity ->
                _childrenInfoViewState.update {
                    it.copy(
                        children = entity
                    )
                }
            }
            onError { error ->
                _childrenInfoViewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _childrenInfoViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun resetCurrentChild() {
        communicator.currentChildId = ""
    }

    private fun deleteChild(childId: String) {
        networkExecutor {
            execute {
                userProfileRepository.deleteChildById(childId)
            }
            onSuccess {
                getChildren()
            }
            onError { error ->
                _childrenInfoViewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _childrenInfoViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun setCurrentChild(childId: String = "") {
        communicator.currentChildId = childId
    }

    private fun consumeRequestError() {
        _childrenInfoViewState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }
}
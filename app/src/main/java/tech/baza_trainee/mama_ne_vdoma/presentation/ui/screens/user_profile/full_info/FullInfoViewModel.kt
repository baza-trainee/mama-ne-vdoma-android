package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

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

class FullInfoViewModel(
    private val communicator: UserProfileCommunicator,
    private val userProfileRepository: UserProfileRepository
): ViewModel() {

    private val _fullInfoViewState = MutableStateFlow(FullInfoViewState())
    val fullInfoViewState: StateFlow<FullInfoViewState> = _fullInfoViewState.asStateFlow()

    init {
        _fullInfoViewState.update {
            it.copy(
                name = communicator.name,
                address = communicator.address,
                userAvatar = communicator.userAvatar
            )
        }

        getChildren()
    }

    private fun getChildren() {
        networkExecutor {
            execute {
                userProfileRepository.getChildren()
            }
            onSuccess { entity ->
                _fullInfoViewState.update {
                    it.copy(
                        children = entity
                    )
                }
            }
            onError { error ->
                _fullInfoViewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _fullInfoViewState.update {
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
                _fullInfoViewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _fullInfoViewState.update {
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

    fun handleFullProfileEvent(event: FullProfileEvent) {
        when(event) {
            FullProfileEvent.UpdateFullProfile -> Unit
            is FullProfileEvent.DeleteChild -> deleteChild(event.id)
            FullProfileEvent.ResetChild -> resetCurrentChild()
            is FullProfileEvent.SetChild -> setCurrentChild(event.id)
            FullProfileEvent.ConsumeRequestError -> consumeRequestError()
        }
    }

    private fun consumeRequestError() {
        _fullInfoViewState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }
}
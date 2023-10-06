package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.decodeBase64ToBitmap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class FullInfoViewModel(
    private val communicator: UserProfileCommunicator,
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _fullInfoViewState = MutableStateFlow(FullInfoViewState())
    val fullInfoViewState: StateFlow<FullInfoViewState> = _fullInfoViewState.asStateFlow()

    init {
        getUserInfo()
        _fullInfoViewState.update {
            it.copy(
                isChildInfoFilled = communicator.isChildInfoFilled,
                isUserInfoFilled = communicator.isUserInfoFilled
            )
        }
    }

    fun handleFullProfileEvent(event: FullProfileEvent) {
        when(event) {
            FullProfileEvent.DeleteUser -> deleteUser()
            is FullProfileEvent.DeleteChild -> deleteChild(event.id)
            FullProfileEvent.ResetChild -> resetCurrentChild()
            is FullProfileEvent.SetChild -> setCurrentChild(event.id)
            FullProfileEvent.ConsumeRequestError -> consumeRequestError()
        }
    }

    private fun deleteUser() {
        networkExecutor {
            execute {
                userProfileRepository.deleteUser()
            }
            onSuccess {
                _fullInfoViewState.update {
                    it.copy(
                        userDeleted = triggered
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

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity?> {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { entity ->
                _fullInfoViewState.update {
                    it.copy(
                        name = entity?.name.orEmpty(),
                        userAvatar = entity?.avatar.orEmpty().decodeBase64ToBitmap(),
                    )
                }

                communicator.apply {
                    name = entity?.name.orEmpty()
                    userAvatar = entity?.avatar.orEmpty().decodeBase64ToBitmap()
                    code = entity?.countryCode.orEmpty()
                    phone = entity?.phone.orEmpty()
                }

                getAddressFromLocation(
                    latLng = LatLng(
                        entity?.location?.coordinates?.get(1) ?: 0.00,
                        entity?.location?.coordinates?.get(0) ?: 0.00
                    )
                )
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

    private fun getAddressFromLocation(latLng: LatLng) {
        networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess { address ->
                communicator.address = address.orEmpty()

                _fullInfoViewState.update {
                    it.copy(
                        address = address.orEmpty()
                    )
                }

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

    private fun consumeRequestError() {
        _fullInfoViewState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }
}
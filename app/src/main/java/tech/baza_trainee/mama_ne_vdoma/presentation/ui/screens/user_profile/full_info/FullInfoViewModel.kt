package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ifNullOrEmpty
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.CommonUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.decodeBase64ToBitmap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class FullInfoViewModel(
    private val communicator: UserProfileCommunicator,
    private val navigator: ScreenNavigator,
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _fullInfoViewState = MutableStateFlow(FullInfoViewState())
    val fullInfoViewState: StateFlow<FullInfoViewState> = _fullInfoViewState.asStateFlow()

    private val _uiState = mutableStateOf<CommonUiState>(CommonUiState.Idle)
    val uiState: State<CommonUiState>
        get() = _uiState

    init {
        getUserInfo()
        _fullInfoViewState.update {
            it.copy(
                isChildInfoFilled = communicator.isChildInfoFilled,
                isUserInfoFilled = communicator.isUserInfoFilled
            )
        }
    }

    fun handleFullProfileEvent(event: FullInfoEvent) {
        when(event) {
            FullInfoEvent.DeleteUser -> deleteUser()
            is FullInfoEvent.DeleteChild -> deleteChild(event.id)
            FullInfoEvent.AddChild -> resetCurrentChild()
            is FullInfoEvent.EditChild -> setCurrentChild(event.id)
            FullInfoEvent.ResetUiState -> _uiState.value = CommonUiState.Idle
            FullInfoEvent.EditUser -> navigator.navigate(UserProfileRoutes.UserInfo)
            FullInfoEvent.OnBack -> navigator.navigate(Graphs.Login)
            FullInfoEvent.OnNext -> TODO()
        }
    }

    private fun deleteUser() {
        networkExecutor {
            execute {
                userProfileRepository.deleteUser()
            }
            onSuccess {
                navigator.navigateOnMain(viewModelScope, Graphs.CreateUser)
            }
            onError { error ->
                _uiState.value = CommonUiState.OnError(error)
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
        networkExecutor<List<ChildEntity>> {
            execute {
                userProfileRepository.getChildren()
            }
            onSuccess { entity ->
                _fullInfoViewState.update {
                    it.copy(
                        children = entity,
                        isChildInfoFilled = entity.isNotEmpty()
                    )
                }
                communicator.isUserInfoFilled = entity.isNotEmpty()
            }
            onError { error ->
                _uiState.value = CommonUiState.OnError(error)
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
                val _isUserInfoFilled = !entity?.name.isNullOrEmpty() &&
                        !entity?.phone.isNullOrEmpty() &&
                        !entity?.schedule?.schedule.isNullOrEmpty()

                val avatar = entity?.avatar.orEmpty()
                val image = if (avatar.isEmpty())
                    BitmapHelper.DEFAULT_BITMAP
                else
                    avatar.decodeBase64ToBitmap()

                _fullInfoViewState.update {
                    it.copy(
                        name = entity?.name.orEmpty(),
                        userAvatar = image,
                        schedule = entity?.schedule.ifNullOrEmpty { ScheduleModel() },
                        isUserInfoFilled = _isUserInfoFilled
                    )
                }

                communicator.apply {
                    isUserInfoFilled = _isUserInfoFilled
                    name = entity?.name.orEmpty()
                    userAvatar = image
                    code = entity?.countryCode.orEmpty()
                    phone = entity?.phone.orEmpty()
                    schedule = entity?.schedule.ifNullOrEmpty { ScheduleModel() }
                }

                if (!entity?.location?.coordinates.isNullOrEmpty())
                    getAddressFromLocation(
                        latLng = LatLng(
                            entity?.location?.coordinates?.get(1) ?: 0.00,
                            entity?.location?.coordinates?.get(0) ?: 0.00
                        )
                    )

                getChildren()
            }
            onError { error ->
                _uiState.value = CommonUiState.OnError(error)
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
            }
            onError { error ->
                _uiState.value = CommonUiState.OnError(error)
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
        navigator.navigate(UserProfileRoutes.ChildInfo)
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
                _uiState.value = CommonUiState.OnError(error)
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
        navigator.navigate(UserProfileRoutes.ChildSchedule)
    }
}
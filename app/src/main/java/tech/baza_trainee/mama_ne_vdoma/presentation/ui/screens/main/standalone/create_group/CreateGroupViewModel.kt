package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.create_group

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.getDefaultSchedule
import tech.baza_trainee.mama_ne_vdoma.domain.model.updateSchedule
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserAuthRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.GroupsInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StandaloneGroupsRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.UpdateDetailsUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.GroupDetailsEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.CropImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.Communicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.MAIN_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.MAX_AGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.MIN_AGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.NAME_LENGTH
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.NOTIFICATIONS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SETTINGS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateName
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek

class CreateGroupViewModel(
    private val childCommunicator: Communicator<String>,
    private val communicator: CropImageCommunicator,
    private val navigator: ScreenNavigator,
    private val groupsInteractor: GroupsInteractor,
    private val userAuthRepository: UserAuthRepository,
    private val userProfileRepository: UserProfileRepository,
    private val bitmapHelper: BitmapHelper,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel(), GroupsInteractor by groupsInteractor, NetworkEventsListener {


    private val _viewState = MutableStateFlow(CreateGroupViewState())
    val viewState: StateFlow<CreateGroupViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<UpdateDetailsUiState>(UpdateDetailsUiState.Idle)
    val uiState: State<UpdateDetailsUiState>
        get() = _uiState

    private var avatarServerPath = ""

    init {
        groupsInteractor.apply {
            setGroupsCoroutineScope(viewModelScope)
            setGroupsNetworkListener(this@CreateGroupViewModel)
        }

        getUserInfo()
        getChildren()

        viewModelScope.launch {
            communicator.croppedImageFlow.collect(::saveGroupAvatar)
        }

        viewModelScope.launch {
            preferencesDatastoreManager.userPreferencesFlow.collect { pref ->
                _viewState.update {
                    it.copy(
                        groupDetails = it.groupDetails.copy(userAvatar =  pref.avatarUri),
                        notifications = pref.myJoinRequests + pref.adminJoinRequests
                    )
                }
            }
        }

        _viewState.update {
            it.copy(
                groupDetails = it.groupDetails.copy(userAvatar = preferencesDatastoreManager.avatarUri)
            )
        }
    }

    override fun onLoading(state: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = state
            )
        }
    }

    override fun onError(error: String) {
        _uiState.value = UpdateDetailsUiState.OnError(error)
    }

    fun handleEvent(event: GroupDetailsEvent) {
        when (event) {
            GroupDetailsEvent.ResetUiState -> _uiState.value = UpdateDetailsUiState.Idle
            GroupDetailsEvent.OnBack -> navigator.goBack()
            is GroupDetailsEvent.OnSave -> createGroup()
            is GroupDetailsEvent.UpdateGroupSchedule -> updateGroupSchedule(event.day, event.period)
            is GroupDetailsEvent.UpdateName -> validateName(event.value)
            is GroupDetailsEvent.UpdateMaxAge -> validateMaxAge(event.value)
            is GroupDetailsEvent.UpdateMinAge -> validateMinAge(event.value)
            is GroupDetailsEvent.UpdateDescription -> updateDescription(event.value)
            GroupDetailsEvent.OnDeletePhoto -> Unit
            GroupDetailsEvent.OnEditPhoto -> navigator.navigate(StandaloneGroupsRoutes.GroupImageCrop)
            is GroupDetailsEvent.SetImageToCrop -> communicator.uriForCrop = event.uri
            GroupDetailsEvent.GoToMain -> {
                childCommunicator.setData("")
                navigator.navigate(HostScreenRoutes.Host.getDestination(MAIN_PAGE))
            }

            GroupDetailsEvent.GetLocationFromAddress -> getLocationFromAddress()
            is GroupDetailsEvent.UpdateGroupAddress -> updateGroupAddress(event.address)

            GroupDetailsEvent.OnAvatarClicked ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(SETTINGS_PAGE))

            GroupDetailsEvent.GoToNotifications ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(NOTIFICATIONS_PAGE))

            else -> Unit
        }
    }

    private fun updateGroupAddress(address: String) {
        _viewState.update {
            it.copy(
                groupDetails = it.groupDetails.copy(
                    address = address,
                    isAddressChecked = false
                )
            )
        }
    }

    private fun getLocationFromAddress() {
        getGroupLocation(_viewState.value.groupDetails.address) { location ->
            location?.let {
                _viewState.update {
                    it.copy(
                        groupDetails = it.groupDetails.copy(
                            location = location,
                            isAddressChecked = true
                        )
                    )
                }
            } ?: run {
                _uiState.value = UpdateDetailsUiState.AddressNotFound
            }
        }
    }

    private fun updateGroupLocation(location: LatLng, groupId: String) {
        updateGroupLocation(location, groupId) {
            //do nothing
        }
    }

    private fun saveGroupAvatar(image: Bitmap) {
        if (communicator.justCropped) {
            bitmapHelper.resizeBitmap(
                scope = viewModelScope,
                image = image,
                onSuccess = { bmp ->
                    _viewState.update {
                        it.copy(
                            groupDetails = it.groupDetails.copy(avatar = bmp)
                        )
                    }
                    communicator.justCropped = false
                    communicator.setCroppedImage(null)
                },
                onError = {
                    _uiState.value = UpdateDetailsUiState.OnAvatarError
                }
            )
        }
    }

    private fun uploadAvatar(image: Bitmap, groupId: String) {
        uploadGroupAvatar(image) {
            avatarServerPath = it
            updateGroup(groupId)
        }
    }

    private fun createGroup() {
        if (_viewState.value.groupDetails.isAddressChecked) {
            createGroup(
                childCommunicator.dataFlow.value.orEmpty(),
                _viewState.value.groupDetails.name,
                _viewState.value.groupDetails.description
            ) {
                childCommunicator.setData("")
                uploadAvatar(_viewState.value.groupDetails.avatar, it.id)
                updateGroupLocation(_viewState.value.groupDetails.location, it.id)
            }
        } else _uiState.value = UpdateDetailsUiState.AddressNotChecked
    }

    private fun updateGroup(groupId: String) {
        val ages =
            if (_viewState.value.groupDetails.minAge == _viewState.value.groupDetails.maxAge) _viewState.value.groupDetails.minAge
            else "${_viewState.value.groupDetails.minAge}-${_viewState.value.groupDetails.maxAge}"
        updateGroup(
            groupId,
            _viewState.value.groupDetails.name,
            _viewState.value.groupDetails.description,
            ages,
            avatarServerPath,
            _viewState.value.groupDetails.schedule
        ) {
            _uiState.value = UpdateDetailsUiState.OnSaved
        }
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity> {
            execute {
                userAuthRepository.getUserInfo()
            }
            onSuccess { entity ->
                if (entity.location.coordinates.isNotEmpty()) {
                    val location = LatLng(
                        entity.location.coordinates[1],
                        entity.location.coordinates[0]
                    )
                    getAddressFromLocation(
                        latLng = location
                    )
                    _viewState.update {
                        it.copy(
                            groupDetails = it.groupDetails.copy(
                                location = location
                            )
                        )
                    }
                }
            }
            onError { error ->
                _uiState.value = UpdateDetailsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getAddressFromLocation(latLng: LatLng) {
        getGroupAddress(latLng) { address ->
            _viewState.update {
                it.copy(
                    groupDetails = it.groupDetails.copy(
                        address = address
                    )
                )
            }
        }
    }

    private fun getChildren() {
        networkExecutor<List<ChildEntity>> {
            execute {
                userProfileRepository.getChildren()
            }
            onSuccess { entity ->
                val child = entity.find { it.childId == childCommunicator.dataFlow.value }
                _viewState.update { state ->
                    state.copy(
                        groupDetails = state.groupDetails.copy(
                            schedule = child?.schedule ?: getDefaultSchedule(),
                            minAge = child?.age.orEmpty(),
                            minAgeValid = ValidField.VALID,
                            maxAge = child?.age.orEmpty(),
                            maxAgeValid = ValidField.VALID
                        )
                    )
                }
            }
            onError { error ->
                _uiState.value = UpdateDetailsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun updateGroupSchedule(dayOfWeek: DayOfWeek, dayPeriod: Period) {
        val currentSchedule =
            _viewState.value.groupDetails.schedule.updateSchedule(dayOfWeek, dayPeriod)
        _viewState.update {
            it.copy(
                groupDetails = it.groupDetails.copy(
                    schedule = currentSchedule
                )
            )
        }
    }

    private fun validateName(name: String) {
        val nameValid = if (name.validateName(NAME_LENGTH)) ValidField.VALID
        else ValidField.INVALID
        _viewState.update {
            it.copy(
                groupDetails = it.groupDetails.copy(
                    name = name,
                    nameValid = nameValid
                )
            )
        }
    }

    private fun validateMinAge(age: String) {
        age.toIntOrNull()?.let { minAge ->
            val maxAge = _viewState.value.groupDetails.maxAge.toIntOrNull() ?: minAge
            val minAgeValid =
                if (minAge in MIN_AGE..maxAge) ValidField.VALID
                else ValidField.INVALID
            _viewState.update {
                it.copy(
                    groupDetails = it.groupDetails.copy(
                        minAge = age,
                        minAgeValid = minAgeValid
                    )
                )
            }
        } ?: run {
            _viewState.update {
                it.copy(
                    groupDetails = it.groupDetails.copy(
                        minAge = age,
                        minAgeValid = ValidField.EMPTY
                    )
                )
            }
        }
    }

    private fun validateMaxAge(age: String) {
        age.toIntOrNull()?.let { maxAge ->
            val minAge = _viewState.value.groupDetails.minAge.toIntOrNull() ?: maxAge
            val maxAgeValid =
                if (maxAge in minAge..MAX_AGE) ValidField.VALID
                else ValidField.INVALID
            _viewState.update {
                it.copy(
                    groupDetails = it.groupDetails.copy(
                        maxAge = age,
                        maxAgeValid = maxAgeValid
                    )
                )
            }
        } ?: run {
            _viewState.update {
                it.copy(
                    groupDetails = it.groupDetails.copy(
                        maxAge = age,
                        maxAgeValid = ValidField.EMPTY
                    )
                )
            }
        }
    }

    private fun updateDescription(desc: String) {
        _viewState.update {
            it.copy(
                groupDetails = it.groupDetails.copy(
                    description = desc
                )
            )
        }
    }
}
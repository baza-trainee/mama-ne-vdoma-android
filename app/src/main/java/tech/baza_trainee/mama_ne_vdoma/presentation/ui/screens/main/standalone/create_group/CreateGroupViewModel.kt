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
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserAuthRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StandaloneGroupsRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.GroupSearchCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.CropImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.MAIN_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SETTINGS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek

class CreateGroupViewModel(
    private val childCommunicator: GroupSearchCommunicator,
    private val communicator: CropImageCommunicator,
    private val navigator: ScreenNavigator,
    private val userAuthRepository: UserAuthRepository,
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val locationRepository: LocationRepository,
    private val groupsRepository: GroupsRepository,
    private val bitmapHelper: BitmapHelper,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {


    private val _viewState = MutableStateFlow(CreateGroupViewState())
    val viewState: StateFlow<CreateGroupViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<CreateGroupUiState>(CreateGroupUiState.Idle)
    val uiState: State<CreateGroupUiState>
        get() = _uiState

    private var avatarServerPath = ""

    init {
        getUserInfo()
        getChildren()

        viewModelScope.launch {
            communicator.croppedImageFlow.collect(::saveGroupAvatar)
        }

        _viewState.update {
            it.copy(userAvatar = preferencesDatastoreManager.avatarUri)
        }
    }

    fun handleEvent(event: CreateGroupEvent) {
        when(event) {
            CreateGroupEvent.ResetUiState -> _uiState.value = CreateGroupUiState.Idle
            CreateGroupEvent.OnBack -> navigator.goBack()
            CreateGroupEvent.OnCreate -> createGroup()
            is CreateGroupEvent.UpdateGroupSchedule -> updateGroupSchedule(event.day, event.period)
            is CreateGroupEvent.UpdateName -> validateName(event.value)
            is CreateGroupEvent.UpdateMaxAge -> validateMaxAge(event.value)
            is CreateGroupEvent.UpdateMinAge -> validateMinAge(event.value)
            is CreateGroupEvent.UpdateDescription -> updateDescription(event.value)
            CreateGroupEvent.OnDeletePhoto -> Unit
            CreateGroupEvent.OnEditPhoto -> navigator.navigate(StandaloneGroupsRoutes.GroupImageCrop)
            is CreateGroupEvent.SetImageToCrop -> communicator.uriForCrop = event.uri
            CreateGroupEvent.GoToMain -> {
                childCommunicator.childId = ""
                navigator.navigate(HostScreenRoutes.Host.getDestination(MAIN_PAGE))
            }

            CreateGroupEvent.GetLocationFromAddress -> getLocationFromAddress()
            is CreateGroupEvent.UpdateGroupAddress -> updateGroupAddress(event.address)
            CreateGroupEvent.OnAvatarClicked ->
                navigator.navigate(HostScreenRoutes.Host.getDestination(SETTINGS_PAGE))
        }
    }

    private fun updateGroupAddress(address: String) {
        _viewState.update {
            it.copy(
                address = address,
                isAddressChecked = false
            )
        }
    }

    private fun getLocationFromAddress() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getLocationFromAddress(_viewState.value.address)
            }
            onSuccess { location ->
                location?.let {
                    _viewState.update {
                        it.copy(
                            location = location,
                            isAddressChecked = true
                        )
                    }
                } ?: run {
                    _uiState.value = CreateGroupUiState.AddressNotFound
                }
            }
            onError { error ->
                _uiState.value = CreateGroupUiState.OnError(error)
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

    private fun updateGroupLocation(location: LatLng, groupId: String) {
        networkExecutor {
            execute {
                groupsRepository.updateGroupLocation(
                    groupId,
                    location.latitude,
                    location.longitude
                )
            }
            onError { error ->
                _uiState.value = CreateGroupUiState.OnError(error)
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

    private fun saveGroupAvatar(image: Bitmap) {
        if (communicator.justCropped) {
            bitmapHelper.resizeBitmap(
                scope = viewModelScope,
                image = image,
                onSuccess = { bmp ->
                    _viewState.update {
                        it.copy(avatar = bmp)
                    }
                    communicator.justCropped = false
                    communicator.setCroppedImage(null)
                },
                onError = {
                    _uiState.value = CreateGroupUiState.OnAvatarError
                }
            )
        }
    }

    private fun uploadAvatar(image: Bitmap, groupId: String) {
        networkExecutor {
            execute {
                filesRepository.saveAvatar(image)
            }
            onSuccess {
                avatarServerPath = it
                updateGroup(groupId)
            }
            onError { error ->
                _uiState.value = CreateGroupUiState.OnError(error)
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

    private fun createGroup() {
        if (_viewState.value.isAddressChecked) {
            networkExecutor<GroupEntity> {
                execute {
                    groupsRepository.createGroup(
                        childCommunicator.childId,
                        _viewState.value.name,
                        _viewState.value.description
                    )
                }
                onSuccess {
                    childCommunicator.childId = ""
                    uploadAvatar(_viewState.value.avatar, it.id)
                    updateGroupLocation(_viewState.value.location, it.id)
                }
                onError { error ->
                    _uiState.value = CreateGroupUiState.OnError(error)
                }
                onLoading { isLoading ->
                    _viewState.update {
                        it.copy(
                            isLoading = isLoading
                        )
                    }
                }
            }
        } else _uiState.value = CreateGroupUiState.AddressNotChecked
    }

    private fun updateGroup(groupId: String) {
        networkExecutor {
            execute {
                val ages = if(_viewState.value.minAge == _viewState.value.maxAge) _viewState.value.minAge
                else "${_viewState.value.minAge}-${_viewState.value.maxAge}"
                groupsRepository.updateGroup(
                    groupId,
                    UpdateGroupEntity(
                        name = _viewState.value.name,
                        desc = _viewState.value.description,
                        ages = ages,
                        schedule = _viewState.value.schedule,
                        avatar = avatarServerPath
                    )
                )
            }
            onSuccess {
                _uiState.value = CreateGroupUiState.OnGroupCreated
            }
            onError { error ->
                _uiState.value = CreateGroupUiState.OnError(error)
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
                            location = location
                        )
                    }
                }

            }
            onError { error ->
                _uiState.value = CreateGroupUiState.OnError(error)
            }
            onLoading { isLoading ->

            }
        }
    }

    private fun getAddressFromLocation(latLng: LatLng) {
        networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess { address ->
                _viewState.update {
                    it.copy(
                        address = address.orEmpty()
                    )
                }
            }
            onError { error ->
                _uiState.value = CreateGroupUiState.OnError(error)
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

    private fun getChildren() {
        networkExecutor<List<ChildEntity>> {
            execute {
                userProfileRepository.getChildren()
            }
            onSuccess { entity ->
                val child = entity.find { it.childId == childCommunicator.childId }
                _viewState.update { state ->
                    state.copy(
                        schedule = child?.schedule ?: ScheduleModel(),
                        minAge = child?.age.orEmpty(),
                        minAgeValid = ValidField.VALID,
                        maxAge = child?.age.orEmpty(),
                        maxAgeValid = ValidField.VALID
                    )
                }
            }
            onError { error ->
                _uiState.value = CreateGroupUiState.OnError(error)
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
        val currentSchedule = _viewState.value.schedule
        when (dayPeriod) {
            Period.WHOLE_DAY -> {
                _viewState.update {
                    it.copy(
                        schedule = currentSchedule.apply {
                            schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                wholeDay = schedule[dayOfWeek]?.wholeDay?.not() ?: false
                            ) ?: DayPeriod()
                            if (schedule[dayOfWeek]?.wholeDay == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    morning = false,
                                    noon = false,
                                    afternoon = false
                                ) ?: DayPeriod()
                            }
                        }
                    )
                }
            }

            Period.MORNING -> {
                _viewState.update {
                    it.copy(
                        schedule = currentSchedule.apply {
                            schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                morning = schedule[dayOfWeek]?.morning?.not() ?: false
                            ) ?: DayPeriod()
                            if (schedule[dayOfWeek]?.morning == true &&
                                schedule[dayOfWeek]?.noon == true &&
                                schedule[dayOfWeek]?.afternoon == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = true,
                                    morning = false,
                                    noon = false,
                                    afternoon = false
                                ) ?: DayPeriod()
                            } else if (schedule[dayOfWeek]?.morning == true && schedule[dayOfWeek]?.wholeDay == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = false
                                ) ?: DayPeriod()
                            }
                        }
                    )
                }
            }

            Period.NOON -> {
                _viewState.update {
                    it.copy(
                        schedule = currentSchedule.apply {
                            schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                noon = schedule[dayOfWeek]?.noon?.not() ?: false
                            ) ?: DayPeriod()
                            if (schedule[dayOfWeek]?.morning == true &&
                                schedule[dayOfWeek]?.noon == true &&
                                schedule[dayOfWeek]?.afternoon == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = true,
                                    morning = false,
                                    noon = false,
                                    afternoon = false
                                ) ?: DayPeriod()
                            } else if (schedule[dayOfWeek]?.noon == true && schedule[dayOfWeek]?.wholeDay == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = false
                                ) ?: DayPeriod()
                            }
                        }
                    )
                }
            }

            Period.AFTERNOON -> {
                _viewState.update {
                    it.copy(
                        schedule = currentSchedule.apply {
                            schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                afternoon = schedule[dayOfWeek]?.afternoon?.not() ?: false
                            ) ?: DayPeriod()
                            if (schedule[dayOfWeek]?.morning == true &&
                                schedule[dayOfWeek]?.noon == true &&
                                schedule[dayOfWeek]?.afternoon == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = true,
                                    morning = false,
                                    noon = false,
                                    afternoon = false
                                ) ?: DayPeriod()
                            } else if (schedule[dayOfWeek]?.afternoon == true && schedule[dayOfWeek]?.wholeDay == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = false
                                ) ?: DayPeriod()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun validateName(name: String) {
        val nameValid = if (name.length in NAME_LENGTH &&
            name.all { it.isLetter() || it.isDigit() || it.isWhitespace() || it == '-' }) ValidField.VALID
        else ValidField.INVALID
        _viewState.update {
            it.copy(
                name = name,
                nameValid = nameValid
            )
        }
    }

    private fun validateMinAge(age: String) {
        age.toIntOrNull()?.let { minAge ->
            val maxAge = _viewState.value.maxAge.toIntOrNull() ?: minAge
            val minAgeValid =
                if (minAge in MIN_AGE..maxAge) ValidField.VALID
                else ValidField.INVALID
            _viewState.update {
                it.copy(
                    minAge = age,
                    minAgeValid = minAgeValid
                )
            }
        } ?: run {
            _viewState.update {
                it.copy(
                    minAge = age,
                    minAgeValid = ValidField.EMPTY
                )
            }
        }
    }

    private fun validateMaxAge(age: String) {
        age.toIntOrNull()?.let { maxAge ->
            val minAge = _viewState.value.minAge.toIntOrNull() ?: maxAge
            val maxAgeValid =
                if (maxAge in minAge..MAX_AGE) ValidField.VALID
                else ValidField.INVALID
            _viewState.update {
                it.copy(
                    maxAge = age,
                    maxAgeValid = maxAgeValid
                )
            }
        } ?: run {
            _viewState.update {
                it.copy(
                    maxAge = age,
                    maxAgeValid = ValidField.EMPTY
                )
            }
        }
    }

    private fun updateDescription(desc: String) {
        _viewState.update {
            it.copy(
                description = desc
            )
        }
    }

    companion object {

        private val NAME_LENGTH = 6..18
        private const val MIN_AGE = 1
        private const val MAX_AGE = 18
    }
}
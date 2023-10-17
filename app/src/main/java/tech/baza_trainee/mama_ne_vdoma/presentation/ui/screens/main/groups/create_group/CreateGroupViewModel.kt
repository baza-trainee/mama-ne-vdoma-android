package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.create_group

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.CreateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GroupImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek

class CreateGroupViewModel(
    private val childId: String,
    private val communicator: GroupImageCommunicator,
    private val navigator: PageNavigator,
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository,
    private val groupsRepository: GroupsRepository,
    private val bitmapHelper: BitmapHelper
): ViewModel() {


    private val _viewState = MutableStateFlow(CreateGroupViewState())
    val viewState: StateFlow<CreateGroupViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<CreateGroupUiState>(CreateGroupUiState.Idle)
    val uiState: State<CreateGroupUiState>
        get() = _uiState

    init {
        getUserInfo()
        getChildren()
        if (communicator.croppedImage != BitmapHelper.DEFAULT_BITMAP) {
            saveGroupAvatar(communicator.croppedImage)
            communicator.croppedImage = BitmapHelper.DEFAULT_BITMAP
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
            CreateGroupEvent.OnDeletePhoto -> TODO()
            CreateGroupEvent.OnEditPhoto -> navigator.navigate(GroupsScreenRoutes.ImageCrop)
            is CreateGroupEvent.SetImageToCrop -> communicator.uriForCrop = event.uri
        }
    }

    private fun saveGroupAvatar(image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val newImage = if (image.height > IMAGE_HEIGHT)
                Bitmap.createScaledBitmap(
                    image,
                    IMAGE_WIDTH,
                    IMAGE_HEIGHT,
                    true
                )
            else image
            val newImageSize = bitmapHelper.getSize(newImage)
            if (newImageSize < IMAGE_SIZE) {
                _viewState.update {
                    it.copy(
                        avatar = newImage
                    )
                }
                //uploadUserAvatar(newImage)
            } else {
                _uiState.value = CreateGroupUiState.OnAvatarError
            }
        }
    }

    private fun createGroup() {
        networkExecutor<GroupEntity> {
            execute {
                groupsRepository.createGroup(
                    childId,
                    CreateGroupEntity(
                        _viewState.value.name,
                        _viewState.value.description
                    )
                )
            }
            onSuccess { updateGroup(it.id) }
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
                        schedule = _viewState.value.schedule
                    )
                )
            }
            onSuccess { navigator.navigate(GroupsScreenRoutes.Groups) }
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
        networkExecutor<UserProfileEntity?> {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { entity ->
                if (!entity?.location?.coordinates.isNullOrEmpty())
                    getAddressFromLocation(
                        latLng = LatLng(
                            entity?.location?.coordinates?.get(1) ?: 0.00,
                            entity?.location?.coordinates?.get(0) ?: 0.00
                        )
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
                val child = entity.find { it.childId == childId }
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
        val intAge = age.toIntOrNull()
        intAge?.let {
            val minAgeValid =
                if (intAge >= MIN_AGE && intAge <= _viewState.value.maxAge.toInt()) ValidField.VALID
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
        val intAge = age.toIntOrNull()
        intAge?.let {
            val maxAgeValid =
                if (age.toInt() <= MAX_AGE && age.toInt() >= _viewState.value.minAge.toInt()) ValidField.VALID
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
        private const val IMAGE_SIZE = 1 * 1024 * 1024
        private const val IMAGE_HEIGHT = 960
        private const val IMAGE_WIDTH = 360
    }
}
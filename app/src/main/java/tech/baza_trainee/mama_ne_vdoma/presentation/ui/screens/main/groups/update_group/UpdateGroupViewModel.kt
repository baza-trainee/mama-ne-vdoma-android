package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.update_group

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.updateSchedule
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.GroupsInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.UpdateDetailsUiState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.GroupDetailsEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.CropImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.Communicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.MAX_AGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.MIN_AGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.NAME_LENGTH
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateName
import java.time.DayOfWeek

class UpdateGroupViewModel(
    private val communicator: CropImageCommunicator,
    private val groupUpdateCommunicator: Communicator<GroupUiModel>,
    private val navigator: PageNavigator,
    private val groupsInteractor: GroupsInteractor,
    private val bitmapHelper: BitmapHelper,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel(), GroupsInteractor by groupsInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(UpdateGroupViewState())
    val viewState: StateFlow<UpdateGroupViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<UpdateDetailsUiState>(UpdateDetailsUiState.Idle)
    val uiState: StateFlow<UpdateDetailsUiState>
        get() = _uiState.asStateFlow()

    private var groupId = ""

    init {
        groupsInteractor.apply {
            setGroupsCoroutineScope(viewModelScope)
            setGroupsNetworkListener(this@UpdateGroupViewModel)
        }

        viewModelScope.launch {
            communicator.croppedImageFlow.collect(::saveGroupAvatar)
        }

        viewModelScope.launch {
            groupUpdateCommunicator.dataFlow.collect {
                setGroupForEdit(it)
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
            it.copy(isLoading = state)
        }
    }

    override fun onError(error: String) {
        _uiState.update { UpdateDetailsUiState.OnError(error) }
    }

    fun handleEvent(event: GroupDetailsEvent) {
        when (event) {
            GroupDetailsEvent.ResetUiState ->
                _uiState.update { UpdateDetailsUiState.Idle }
            GroupDetailsEvent.OnBack -> navigator.goBack()
            GroupDetailsEvent.OnSave -> updateGroup()
            is GroupDetailsEvent.UpdateGroupSchedule -> updateGroupSchedule(event.day, event.period)
            is GroupDetailsEvent.UpdateName -> validateName(event.value)
            is GroupDetailsEvent.UpdateMaxAge -> validateMaxAge(event.value)
            is GroupDetailsEvent.UpdateMinAge -> validateMinAge(event.value)
            is GroupDetailsEvent.UpdateDescription -> updateDescription(event.value)
            GroupDetailsEvent.OnDeletePhoto -> Unit
            GroupDetailsEvent.OnEditPhoto -> navigator.navigate(GroupsScreenRoutes.UpdateGroupAvatar)
            is GroupDetailsEvent.SetImageToCrop -> communicator.uriForCrop = event.uri
            GroupDetailsEvent.GoToMain -> navigator.navigate(MainScreenRoutes.Main)
            GroupDetailsEvent.GetLocationFromAddress -> getLocationFromAddress()
            is GroupDetailsEvent.UpdateGroupAddress -> updateGroupAddress(event.address)
            is GroupDetailsEvent.OnKick -> event.children.forEach { kickUser(it) }
            else -> Unit
        }
    }

    private fun setGroupForEdit(group: GroupUiModel) {
        groupId = group.id
        val delimiterIndex = group.ages.indexOf("-")
        _viewState.update {
            it.copy(
                groupDetails = it.groupDetails.copy(
                    adminId = group.adminId,
                    members = group.members,
                    isAddressChecked = false,
                    location = group.location,
                    name = group.name,
                    nameValid = ValidField.VALID,
                    description = group.description,
                    minAge = if (delimiterIndex != -1) group.ages.substring(0, delimiterIndex) else group.ages,
                    minAgeValid = ValidField.VALID,
                    maxAge = if (delimiterIndex != -1) group.ages.substring(delimiterIndex + 1) else group.ages,
                    maxAgeValid = ValidField.VALID,
                    schedule = group.schedule
                )
            )
        }

        getGroupAddress(group.location) { location ->
            _viewState.update {
                it.copy(
                    groupDetails = it.groupDetails.copy(
                        address = location,
                        isAddressChecked = true
                    )
                )
            }
        }

        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    groupDetails = it.groupDetails.copy(
                        avatar = bitmapHelper.bitmapFromUri(group.avatar)
                    )
                )
            }
        }
    }

    private fun kickUser(childId: String) {
        kickUser(groupId, childId) {
            // do nothing
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
                _uiState.update { UpdateDetailsUiState.AddressNotFound }
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
                        it.copy(
                            groupDetails = it.groupDetails.copy(avatar = bmp)
                        )
                    }
                    communicator.justCropped = false
                    communicator.setCroppedImage(null)
                },
                onError = {
                    _uiState.update { UpdateDetailsUiState.OnAvatarError }
                }
            )
        }
    }

    private fun updateGroup() = with(_viewState.value.groupDetails) {
        if (isAddressChecked) {
            uploadGroupAvatar(avatar) { avatarPath ->
                updateGroupLocation(location, groupId) {
                    val ages =
                        if (minAge == maxAge) minAge
                        else "${minAge}-${maxAge}"
                    updateGroup(
                        groupId,
                        name,
                        description,
                        ages,
                        avatarPath,
                        schedule
                    ) {
                        _uiState.update { UpdateDetailsUiState.OnSaved }
                    }
                }
            }
        } else _uiState.update { UpdateDetailsUiState.AddressNotChecked }
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
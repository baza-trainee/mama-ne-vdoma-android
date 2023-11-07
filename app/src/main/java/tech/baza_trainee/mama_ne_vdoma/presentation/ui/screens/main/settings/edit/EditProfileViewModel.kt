package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
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
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.LocationInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserProfileInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.CropImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.common.EditProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import java.time.DayOfWeek

class EditProfileViewModel(
    private val mainNavigator: ScreenNavigator,
    private val navigator: PageNavigator,
    private val communicator: CropImageCommunicator,
    private val profileCommunicator: EditProfileCommunicator,
    private val userProfileInteractor: UserProfileInteractor,
    private val locationInteractor: LocationInteractor,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel(), UserProfileInteractor by userProfileInteractor, LocationInteractor by locationInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(EditProfileViewState())
    val viewState: StateFlow<EditProfileViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<EditProfileUiState>(EditProfileUiState.Idle)
    val uiState: State<EditProfileUiState>
        get() = _uiState

    private val childrenToRemove = mutableSetOf<String>()

    private var newAvatar = BitmapHelper.DEFAULT_BITMAP

    init {
        userProfileInteractor.apply {
            setUserProfileCoroutineScope(viewModelScope)
            setUserProfileNetworkListener(this@EditProfileViewModel)
        }
        locationInteractor.apply {
            setLocationCoroutineScope(viewModelScope)
            setLocationNetworkListener(this@EditProfileViewModel)
        }

        _viewState.update {
            it.copy(userAvatar = preferencesDatastoreManager.avatarUri)
        }

        getUserInfo()

        viewModelScope.launch {
            communicator.croppedImageFlow.collect(::saveUserAvatar)
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
        _uiState.value = EditProfileUiState.OnError(error)
    }

    fun handleEvent(event: EditProfileEvent) {
        when(event) {
            EditProfileEvent.DeleteUser -> deleteUser()
            is EditProfileEvent.DeleteChild -> deleteChild(event.id)
            EditProfileEvent.ResetUiState -> _uiState.value = EditProfileUiState.Idle
            EditProfileEvent.OnBack -> navigator.goToPrevious()
            EditProfileEvent.SaveInfo -> saveChanges { _uiState.value = EditProfileUiState.OnProfileSaved }
            EditProfileEvent.GetLocationFromAddress -> getLocationFromAddress()
            EditProfileEvent.OnDeletePhoto -> deleteUserAvatar()
            EditProfileEvent.OnEditPhoto -> navigator.navigate(SettingsScreenRoutes.EditProfilePhoto)
            is EditProfileEvent.OnMapClick -> setLocation(event.location)
            EditProfileEvent.RequestUserLocation -> requestCurrentLocation()
            is EditProfileEvent.SetCode -> setCode(event.code, event.country)
            is EditProfileEvent.SetImageToCrop -> communicator.uriForCrop = event.uri
            is EditProfileEvent.UpdateUserAddress -> updateUserAddress(event.address)
            is EditProfileEvent.ValidatePhone -> validatePhone(event.phone)
            is EditProfileEvent.ValidateUserName -> validateUserName(event.name)
            is EditProfileEvent.SaveParentInfo -> {
                updateParentSchedule(event.schedule)
                updateParentNote(event.note)
            }
            is EditProfileEvent.SaveChildren ->  saveChildrenInfo(event.schedules, event.notes)

            EditProfileEvent.AddChild -> navigator.navigate(SettingsScreenRoutes.ChildInfo)
            EditProfileEvent.OnSaveAndBack -> saveChanges { navigator.goToPrevious() }
            EditProfileEvent.OnSaveAndAddChild -> saveChanges { navigator.navigate(SettingsScreenRoutes.ChildInfo) }
            EditProfileEvent.GoToMain -> navigator.navigate(MainScreenRoutes.Main)
        }
    }

    private fun saveChanges(onFinish: () -> Unit = {}) {
        if (newAvatar != BitmapHelper.DEFAULT_BITMAP) {
            uploadUserAvatar(newAvatar) {
                saveParent()
            }
        } else
            saveParent()

        saveChildren()

        if (childrenToRemove.isNotEmpty())
            childrenToRemove.forEach {
                deleteChild(it) {}
            }

        onFinish()
    }

    private fun saveParent() {
        updateParent(
            UserInfoEntity(
                name = preferencesDatastoreManager.name,
                phone = preferencesDatastoreManager.phone,
                countryCode = preferencesDatastoreManager.code,
                avatar = preferencesDatastoreManager.avatar,
                schedule = _viewState.value.schedule,
                note = _viewState.value.note,
                sendingEmails = preferencesDatastoreManager.sendEmail
            )
        ) {
            saveUserLocation(_viewState.value.currentLocation) {
                profileCommunicator.setProfileChanged(true)
            }
        }
    }

    private fun saveChildren() {
        _viewState.value.children.forEach {
            patchChild(it.childId, it.note, it.schedule) {
                profileCommunicator.setProfileChanged(true)
            }
        }
    }

    private fun updateParentSchedule(schedule: SnapshotStateMap<DayOfWeek, DayPeriod>) {
        _viewState.update {
            it.copy(
                schedule = schedule
            )
        }
    }

    private fun updateParentNote(value: String) {
        _viewState.update {
            it.copy(
                note = value
            )
        }
    }

    private fun saveChildrenInfo(schedules: Map<Int, SnapshotStateMap<DayOfWeek, DayPeriod>>, notes: Map<Int, String>) {
        val children = mutableListOf<ChildEntity>()
        _viewState.value.children.forEachIndexed { index, childEntity ->
            val newChild = childEntity.copy(
                schedule = schedules[index] ?: childEntity.schedule,
                note = notes[index] ?: childEntity.note
            )
            children.add(index, newChild)
        }
        _viewState.update {
            it.copy(
                children = children
            )
        }
    }

    private fun deleteUser() {
        deleteUser {
            mainNavigator.navigate(Graphs.Start)
        }
    }

    private fun getChildren() {
        getChildren { entity ->
            _viewState.update {
                it.copy(
                    children = entity
                )
            }
        }
    }

    private fun getUserInfo() {
        getUserInfo { entity ->
            _viewState.update {
                it.copy(
                    name = entity.name,
                    nameValid = ValidField.VALID,
                    code = entity.countryCode,
                    phone = entity.phone,
                    phoneValid = ValidField.VALID,
                    schedule = entity.schedule
                )
            }

            if (entity.location.coordinates.isNotEmpty()) {
                val location = LatLng(
                    entity.location.coordinates[1],
                    entity.location.coordinates[0]
                )

                getAddressFromLocation(location)

                _viewState.update {
                    it.copy (currentLocation = location)
                }
            }

            getChildren()
        }
    }

    private fun getAddressFromLocation(latLng: LatLng) {
        getAddressFromLocation(latLng) { address ->
            preferencesDatastoreManager.address = address
            _viewState.update {
                it.copy(
                    address = address
                )
            }
        }
    }

    private fun deleteChild(childId: String) {
        childrenToRemove.add(childId)
        val children = _viewState.value.children.toMutableList()
        val childToRemove = children.first { it.childId == childId }
        children.remove(childToRemove)
        _viewState.update {
            it.copy(children = children)
        }
    }

    private fun getLocationFromAddress() {
        getLocationFromAddress(_viewState.value.address) { location ->
            if (_viewState.value.currentLocation != location)
                _viewState.update {
                    it.copy(
                        currentLocation = location
                    )
                }
            preferencesDatastoreManager.apply {
                latitude = location.latitude
                longitude = location.longitude
            }
        }
    }

    private fun deleteUserAvatar() {
        deleteUserAvatar {
            _viewState.update {
                it.copy(
                    userAvatar = Uri.EMPTY
                )
            }
        }
    }

    private fun setCode(code: String, country: String) {
        preferencesDatastoreManager.code = code
        _viewState.update {
            it.copy(
                code = code,
                country = country
            )
        }
    }

    private fun validatePhone(phone: String) {
        validatePhone(phone, _viewState.value.country) { valid ->
            _viewState.update {
                it.copy(
                    phone = phone,
                    phoneValid = valid
                )
            }
        }
    }

    private fun validateUserName(name: String) {
        validateName(name) { valid ->
            _viewState.update {
                it.copy(
                    name =  name,
                    nameValid = valid
                )
            }
        }
    }

    private fun requestCurrentLocation() {
        requestCurrentLocation { location ->
            _viewState.update {
                it.copy(
                    currentLocation = location
                )
            }

            getAddressFromLocation(location)

            preferencesDatastoreManager.apply {
                latitude = location.latitude
                longitude = location.latitude
            }
        }
    }

    private fun setLocation(location: LatLng) {
        _viewState.update {
            it.copy(
                currentLocation = location
            )
        }
        getAddressFromLocation(location)
    }

    private fun updateUserAddress(address: String) {
        preferencesDatastoreManager.address = address
        _viewState.update {
            it.copy(
                address = address
            )
        }
    }

    private fun saveUserAvatar(image: Bitmap) {
        if (communicator.justCropped)
            saveUserAvatar(
                avatar = image,
                onSuccess = { newImage, uri ->
                    _viewState.update {
                        it.copy(
                            userAvatar = uri
                        )
                    }
                    newAvatar = newImage
                    communicator.justCropped = false
                    communicator.setCroppedImage(null)
                },
                onError = {
                    _uiState.value = EditProfileUiState.OnAvatarError
                }
            )
    }
}
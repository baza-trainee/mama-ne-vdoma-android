package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ifNullOrEmpty
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.LocationInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserProfileInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.CropImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.common.VerifyEmailCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validatePassword
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek

class EditProfileViewModel(
    private val mainNavigator: ScreenNavigator,
    private val navigator: PageNavigator,
    private val communicator: CropImageCommunicator,
    private val emailCommunicator: VerifyEmailCommunicator,
    private val authRepository: AuthRepository,
    private val userProfileInteractor: UserProfileInteractor,
    private val locationInteractor: LocationInteractor,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel(), UserProfileInteractor by userProfileInteractor, LocationInteractor by locationInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(EditProfileViewState())
    val viewState: StateFlow<EditProfileViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<EditProfileUiState>(EditProfileUiState.Idle)
    val uiState: State<EditProfileUiState>
        get() = _uiState

    private var avatarServerPath = ""

    private var backupParentSchedule: Map<DayOfWeek, DayPeriod>? = null
    private var backupParentNote: String? = null
    private var backupChildren: Map<String, Map<DayOfWeek, DayPeriod>>? = null
    private var backupChildrenNotes: Map<String, String>? = null

    init {
        userProfileInteractor.apply {
            setUserProfileCoroutineScope(viewModelScope)
            setUserProfileNetworkListener(this@EditProfileViewModel)
        }
        locationInteractor.apply {
            setLocationCoroutineScope(viewModelScope)
            setLocationNetworkListener(this@EditProfileViewModel)
        }

        viewModelScope.launch {
            emailCommunicator.emailChanged.collect { success ->
                _viewState.update {
                    it.copy(
                        isEmailChanged = success
                    )
                }
            }
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
            EditProfileEvent.OnBack -> navigator.goBack()
            EditProfileEvent.SaveInfo -> saveChanges()
            EditProfileEvent.GetLocationFromAddress -> getLocationFromAddress()
            EditProfileEvent.OnDeletePhoto -> deleteUserAvatar()
            EditProfileEvent.OnEditPhoto -> navigator.goToRoute(SettingsScreenRoutes.EditProfilePhoto)
            is EditProfileEvent.OnMapClick -> setLocation(event.location)
            EditProfileEvent.RequestUserLocation -> requestCurrentLocation()
            is EditProfileEvent.SetCode -> setCode(event.code, event.country)
            is EditProfileEvent.SetImageToCrop -> communicator.uriForCrop = event.uri
            is EditProfileEvent.UpdateUserAddress -> updateUserAddress(event.address)
            is EditProfileEvent.ValidateEmail -> validateEmail(event.email)
            is EditProfileEvent.ValidatePassword -> validatePassword(event.password)
            is EditProfileEvent.ValidatePhone -> validatePhone(event.phone)
            is EditProfileEvent.ValidateUserName -> validateUserName(event.name)
            EditProfileEvent.VerifyEmail -> verifyEmail()
            is EditProfileEvent.EditChildNote -> updateChildNote(event.child, event.note)
            is EditProfileEvent.EditChildSchedule -> updateChildSchedule(event.child, event.dayOfWeek, event.period)
            is EditProfileEvent.EditParentNote -> updateParentNote(event.note)
            is EditProfileEvent.EditParentSchedule -> updateParentSchedule(event.dayOfWeek, event.period)
            is EditProfileEvent.RestoreChild -> restoreChildren()
            EditProfileEvent.RestoreParentInfo -> restoreParent()
            is EditProfileEvent.SaveChildren -> {
                backupChildren = null
                backupChildrenNotes = null
            }
            EditProfileEvent.SaveParentInfo -> {
                backupParentNote = null
                backupParentSchedule = null
            }

            EditProfileEvent.AddChild -> Unit
        }
    }

    private fun verifyEmail() {
        networkExecutor {
            execute {
                authRepository.changeEmailInit(_viewState.value.email)
            }
            onSuccess {
                emailCommunicator.setEmail(_viewState.value.email)
                navigator.goToRoute(SettingsScreenRoutes.VerifyNewEmail)
            }
            onError(::onError)
            onLoading(::onLoading)
        }
    }

    private fun restoreParent() {
        val schedule = mutableStateMapOf<DayOfWeek, DayPeriod>().also { map ->
            DayOfWeek.values().forEach {
                map[it] = (backupParentSchedule?.get(it) ?: DayPeriod()).copy()
            }
        }
        _viewState.update {
            it.copy(
                schedule = ScheduleModel(schedule),
                note = backupParentNote.orEmpty()
            )
        }
        backupParentSchedule = null
        backupParentNote = null
    }

    private fun restoreChildren() {
        val children = _viewState.value.children.map { child ->
            val childSchedule = backupChildren?.get(child.childId)
            val schedule = mutableStateMapOf<DayOfWeek, DayPeriod>().also { map ->
                DayOfWeek.values().forEach {
                    map[it] = (childSchedule?.get(it) ?: DayPeriod()).copy()
                }
            }
            val note = backupChildrenNotes?.get(child.childId).orEmpty()
            child.copy(
                schedule = ScheduleModel(schedule),
                note = note
            )
        }.toList()
        _viewState.update {
            it.copy(children = children)
        }
        backupChildren = null
        backupChildrenNotes = null
    }

    private fun saveChanges() {
        saveParent()
        saveChildren()
    }

    private fun saveParent() {
        updateParent(
            UserInfoEntity(
                name = preferencesDatastoreManager.name,
                phone = preferencesDatastoreManager.phone,
                countryCode = preferencesDatastoreManager.code,
                avatar = avatarServerPath,
                schedule = _viewState.value.schedule,
                note = _viewState.value.note
            )
        ) {}
    }

    private fun saveChildren() {
        _viewState.value.children.forEach {
            patchChild(it.childId, it.note, it.schedule) {}
        }
    }

    private fun updateParentSchedule(dayOfWeek: DayOfWeek, dayPeriod: Period) {
        if (backupParentSchedule == null)
            backupParentSchedule = mutableStateMapOf<DayOfWeek, DayPeriod>().also { map ->
                DayOfWeek.values().forEach {
                    map[it] = (_viewState.value.schedule.schedule[it] ?: DayPeriod()).copy()
                }
            }
        _viewState.update {
            it.copy(
                schedule = updateSchedule(it.schedule, dayOfWeek, dayPeriod)
            )
        }
    }

    private fun updateParentNote(value: String) {
        if (backupParentNote == null)
            backupParentNote = value
        val fieldValid = if (value.length < 1000) ValidField.VALID else ValidField.INVALID
        _viewState.update {
            it.copy(
                note = value,
                noteValid = fieldValid
            )
        }
    }

    private fun updateChildNote(childId: Int, value: String) {
        if (backupChildrenNotes == null) {
            backupChildrenNotes = mutableMapOf<String, String>().apply {
                put(
                    _viewState.value.children[childId].childId,
                    _viewState.value.children[childId].note
                )
            }
        }
        val fieldValid = if (value.length < 1000) ValidField.VALID else ValidField.INVALID
        val children = _viewState.value.children.toMutableList().mapIndexed { index, child ->
            if (index == childId) child.copy(note = value) else child
        }
        val fieldMap = _viewState.value.childrenNotesValid.apply {
            this[childId] = fieldValid
        }
        _viewState.update {
            it.copy(
                children = children,
                childrenNotesValid = fieldMap
            )
        }
    }

    private fun updateChildSchedule(childId: Int, dayOfWeek: DayOfWeek, dayPeriod: Period) {
        if (backupChildren == null) {
            val childSchedule = _viewState.value.children[childId].schedule.schedule
            backupChildren = mutableMapOf<String, Map<DayOfWeek, DayPeriod>>().apply {
                put(
                    _viewState.value.children[childId].childId,
                    mutableStateMapOf<DayOfWeek, DayPeriod>().also { map ->
                        DayOfWeek.values().forEach {
                            map[it] = (childSchedule[it] ?: DayPeriod()).copy()
                        }
                    }
                )
            }
        }
        _viewState.update {
            it.copy(
                schedule = updateSchedule(it.children[childId].schedule, dayOfWeek, dayPeriod)
            )
        }
    }

    private fun deleteUser() {
        deleteUser {
            mainNavigator.navigateOnMain(viewModelScope, Graphs.CreateUser)
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
            avatarServerPath =entity.avatar
            getUserAvatar(entity.avatar)

            _viewState.update {
                it.copy(
                    name = entity.name,
                    nameValid = ValidField.VALID,
                    email = entity.email,
                    emailValid = ValidField.VALID,
                    code = entity.countryCode,
                    phone = entity.phone,
                    phoneValid = ValidField.VALID,
                    schedule = entity.schedule.ifNullOrEmpty { ScheduleModel() }
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

    private fun getUserAvatar(avatarId: String) {
        if (avatarId.isEmpty()) {
            preferencesDatastoreManager.avatar = Uri.EMPTY.toString()

            _viewState.update {
                it.copy(
                    userAvatar = Uri.EMPTY
                )
            }
        } else
            getUsetAvatar(avatarId) { uri ->
                _viewState.update {
                    it.copy(userAvatar = uri)
                }
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
        deleteChild(childId) { getChildren() }
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

    private fun validateEmail(email: String) {
        val emailValid = if (email.validateEmail()) ValidField.VALID
        else ValidField.INVALID
        _viewState.update {
            it.copy(
                email = email,
                emailValid = emailValid
            )
        }
    }

    private fun validatePassword(password: String) {
        val passwordValid = if (password.validatePassword()) ValidField.VALID
        else ValidField.INVALID
        _viewState.update {
            it.copy(
                password = password,
                passwordValid = passwordValid
            )
        }
    }

    private fun saveUserAvatar(image: Bitmap) {
        saveUserAvatar(
            avatar = image,
            onSuccess = { newImage, uri ->
                _viewState.update {
                    it.copy(
                        userAvatar = uri
                    )
                }
                uploadUserAvatar(newImage)
            },
            onError = {
                _uiState.value = EditProfileUiState.OnAvatarError
            }
        )
    }

    private fun uploadUserAvatar(image: Bitmap) {
        uploadUserAvatar(image) {
            avatarServerPath = it
        }
    }
}
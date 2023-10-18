package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ifNullOrEmpty
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.CropImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateEmail
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validatePassword
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class EditProfileViewModel(
    private val navigator: PageNavigator,
    private val communicator: CropImageCommunicator,
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val locationRepository: LocationRepository,
    private val phoneNumberUtil: PhoneNumberUtil,
    private val bitmapHelper: BitmapHelper,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(EditProfileViewState())
    val viewState: StateFlow<EditProfileViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<EditProfileUiState>(EditProfileUiState.Idle)
    val uiState: State<EditProfileUiState>
        get() = _uiState

    private var avatarServerPath = ""

    init {
        getUserInfo()

        viewModelScope.launch {
            communicator.croppedImageFlow.collect(::saveUserAvatar)
        }
    }

    fun handleEvent(event: EditProfileEvent) {
        when(event) {
            EditProfileEvent.DeleteUser -> deleteUser()
            is EditProfileEvent.DeleteChild -> deleteChild(event.id)
            EditProfileEvent.AddChild -> resetCurrentChild()
            is EditProfileEvent.EditChild -> setCurrentChild(event.id)
            EditProfileEvent.ResetUiState -> _uiState.value = EditProfileUiState.Idle
            EditProfileEvent.EditUser -> navigator.navigate(UserProfileRoutes.UserInfo)
            EditProfileEvent.OnBack -> navigator.navigate(Graphs.Login)
            EditProfileEvent.SaveInfo -> Unit
            EditProfileEvent.GetLocationFromAddress -> getLocationFromAddress()
            EditProfileEvent.OnDeletePhoto -> deleteUserAvatar()
            EditProfileEvent.OnEditPhoto -> navigator.navigate(SettingsScreenRoutes.EditProfilePhoto)
            is EditProfileEvent.OnMapClick -> setLocation(event.location)
            EditProfileEvent.RequestUserLocation -> requestCurrentLocation()
            is EditProfileEvent.SetCode -> setCode(event.code, event.country)
            is EditProfileEvent.SetImageToCrop -> communicator.uriForCrop = event.uri
            is EditProfileEvent.UpdatePolicyCheck -> updatePolicyCheck(event.isChecked)
            is EditProfileEvent.UpdateUserAddress -> updateUserAddress(event.address)
            is EditProfileEvent.ValidateEmail -> validateEmail(event.email)
            is EditProfileEvent.ValidatePassword -> validatePassword(event.password)
            is EditProfileEvent.ValidatePhone -> validatePhone(event.phone)
            is EditProfileEvent.ValidateUserName -> validateUserName(event.name)
            EditProfileEvent.VerifyEmail -> Unit // navigator.navigate(CreateUserRoute.VerifyEmail.getDestination(_viewState.value.email))
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
                _uiState.value = EditProfileUiState.OnError(error)
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
                _viewState.update {
                    it.copy(
                        children = entity
                    )
                }
            }
            onError { error ->
                _uiState.value = EditProfileUiState.OnError(error)
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
                userProfileRepository.getUserInfo()
            }
            onSuccess { entity ->
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

//                preferencesDatastoreManager.apply {
//                    name = entity.name
//                    code = entity.countryCode
//                    phone = entity.phone
//                }

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
            onError { error ->
                _uiState.value = EditProfileUiState.OnError(error)
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

    private fun getUserAvatar(avatarId: String) {
        if (avatarId.isEmpty()) {
            preferencesDatastoreManager.avatar = Uri.EMPTY.toString()

            _viewState.update {
                it.copy(
                    userAvatar = Uri.EMPTY
                )
            }
        } else
            networkExecutor {
                execute { filesRepository.getAvatar(avatarId) }
                onSuccess { uri ->
                    preferencesDatastoreManager.avatar = uri.toString()
                    _viewState.update {
                        it.copy(userAvatar = uri)
                    }
                }
                onError { error ->
                    _uiState.value = EditProfileUiState.OnError(error)
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
                preferencesDatastoreManager.address = address.orEmpty()

                _viewState.update {
                    it.copy(
                        address = address.orEmpty()
                    )
                }
            }
            onError { error ->
                _uiState.value = EditProfileUiState.OnError(error)
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

    private fun resetCurrentChild() {
//        communicator.currentChildId = ""
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
                _uiState.value = EditProfileUiState.OnError(error)
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

    private fun setCurrentChild(childId: String = "") {
//        communicator.currentChildId = childId
        navigator.navigate(UserProfileRoutes.ChildSchedule)
    }

    private fun getLocationFromAddress() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getLocationFromAddress(_viewState.value.address)
            }
            onSuccess { location ->
                location?.let {
                    if (_viewState.value.currentLocation != location)
                        _viewState.update {
                            it.copy(
                                currentLocation = location
                            )
                        }
                    preferencesDatastoreManager.apply {
                        latitude = it.latitude
                        longitude = it.longitude
                    }
                }
            }
            onError { error ->
                _uiState.value = EditProfileUiState.OnError(error)
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

    private fun deleteUserAvatar() {
        networkExecutor {
            execute {
                userProfileRepository.deleteUserAvatar()
            }
            onSuccess {
                preferencesDatastoreManager.avatar = Uri.EMPTY.toString()
                _viewState.update {
                    it.copy(
                        userAvatar = Uri.EMPTY
                    )
                }
            }
            onError { error ->
                _uiState.value = EditProfileUiState.OnError(error)
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
        val phoneValid = try {
            val fullNumber = _viewState.value.code + phone
            val phoneNumber = phoneNumberUtil.parse(fullNumber, _viewState.value.country)
            if (phoneNumberUtil.isPossibleNumber(phoneNumber)) ValidField.VALID
            else ValidField.INVALID
        } catch (e: Exception) {
            ValidField.INVALID
        }
        preferencesDatastoreManager.phone = phone
        _viewState.update {
            it.copy(
                phone = phone,
                phoneValid = phoneValid
            )
        }
    }

    private fun validateUserName(name: String) {
        val nameValid = if (name.length in NAME_LENGTH &&
            name.all { it.isLetter() || it.isDigit() || it == ' ' || it == '-' })
            ValidField.VALID
        else
            ValidField.INVALID

        preferencesDatastoreManager.name = name
        _viewState.update {
            it.copy(
                name =  name,
                nameValid = nameValid
            )
        }
    }

    private fun requestCurrentLocation() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getCurrentLocation()
            }
            onSuccess { location ->
                location?.let {
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
            onError { error ->
                _uiState.value = EditProfileUiState.OnError(error)
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

    private fun updatePolicyCheck(isChecked: Boolean) {
        _viewState.update {
            it.copy(
                isPolicyChecked = isChecked
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
        viewModelScope.launch(Dispatchers.IO) {
            val newImage = if (image.height > IMAGE_DIM)
                Bitmap.createScaledBitmap(image, IMAGE_DIM, IMAGE_DIM, true)
            else image
            val newImageSize = bitmapHelper.getSize(newImage)
            if (newImageSize < IMAGE_SIZE) {
                val uri = bitmapHelper.bitmapToFile(newImage).toUri()
                _viewState.update {
                    it.copy(
                        userAvatar = uri
                    )
                }
                uploadUserAvatar(newImage)
            } else {
                _uiState.value = EditProfileUiState.OnAvatarError
            }
        }
    }

    private fun uploadUserAvatar(image: Bitmap) {
        networkExecutor<String> {
            execute {
                filesRepository.saveAvatar(image)
            }
            onSuccess {
                avatarServerPath = it
            }
            onError { error ->
                _uiState.value = EditProfileUiState.OnError(error)
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

    companion object {

        private val NAME_LENGTH = 2..18
        private const val IMAGE_SIZE = 1 * 1024 * 1024
        private const val IMAGE_DIM = 512
    }
}
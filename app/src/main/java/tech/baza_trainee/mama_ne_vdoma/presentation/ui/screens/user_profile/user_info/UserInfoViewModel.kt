package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserInfoInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserInfoInteractorImpl
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class UserInfoViewModel(
    private val communicator: UserProfileCommunicator,
    private val navigator: ScreenNavigator,
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val phoneNumberUtil: PhoneNumberUtil,
    private val bitmapHelper: BitmapHelper,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager,
    userInfoInteractor: UserInfoInteractor = UserInfoInteractorImpl(userProfileRepository, filesRepository, phoneNumberUtil, bitmapHelper, preferencesDatastoreManager)
): ViewModel(), UserInfoInteractor by userInfoInteractor, NetworkEventsListener {

    private val _userInfoScreenState = MutableStateFlow(UserInfoViewState())
    val userInfoScreenState: StateFlow<UserInfoViewState> = _userInfoScreenState.asStateFlow()

    private val _uiState = mutableStateOf<UserInfoUiState>(UserInfoUiState.Idle)
    val uiState: State<UserInfoUiState>
        get() = _uiState

    init {
        userInfoInteractor.apply {
            setCoroutineScope(viewModelScope)
            setNetworkListener(this@UserInfoViewModel)
        }
        _userInfoScreenState.update {
            it.copy(
                name = preferencesDatastoreManager.name,
                nameValid = if (preferencesDatastoreManager.name.isEmpty()) ValidField.EMPTY else ValidField.VALID,
                code = preferencesDatastoreManager.code,
                phone = preferencesDatastoreManager.phone,
                phoneValid = if (preferencesDatastoreManager.phone.isEmpty()) ValidField.EMPTY else ValidField.VALID,
                userAvatar = Uri.parse(preferencesDatastoreManager.avatar)
            )
        }
        if (communicator.croppedImage != BitmapHelper.DEFAULT_BITMAP) {
            saveUserAvatar(communicator.croppedImage)
            communicator.croppedImage = BitmapHelper.DEFAULT_BITMAP
        }
    }

    override fun onLoading(state: Boolean) {
        _userInfoScreenState.update {
            it.copy(
                isLoading = state
            )
        }
    }

    override fun onError(error: String) {
        _uiState.value = UserInfoUiState.OnError(error)
    }

    fun handleUserInfoEvent(event: UserInfoEvent) {
        when(event) {
            is UserInfoEvent.SetImageToCrop -> setUriForCrop(event.uri)
            is UserInfoEvent.ValidateUserName -> validateUserName(event.name)
            is UserInfoEvent.ValidatePhone -> validatePhone(event.phone)
            is UserInfoEvent.SetCode -> setCode(event.code, event.country)
            UserInfoEvent.SaveInfo -> saveUserInfo()
            UserInfoEvent.ResetUiState -> _uiState.value = UserInfoUiState.Idle
            UserInfoEvent.OnEditPhoto -> navigator.navigate(UserProfileRoutes.ImageCrop)
            UserInfoEvent.OnDeletePhoto -> deleteUserAvatar()
            UserInfoEvent.OnBack -> navigator.navigate(UserProfileRoutes.FullProfile)
        }
    }

    private fun setUriForCrop(uri: Uri) {
        communicator.uriForCrop = uri
    }

    private fun deleteUserAvatar() {
        deleteUserAvatar {
            communicator.apply {
                uriForCrop = Uri.EMPTY
                avatarServerPath = null
            }

            _userInfoScreenState.update {
                it.copy(
                    userAvatar = Uri.EMPTY
                )
            }
        }
    }

    private fun saveUserAvatar(image: Bitmap) {
        saveUserAvatar(
            avatar = image,
            onSuccess = { bitmap, uri ->
                _userInfoScreenState.update {
                    it.copy(
                        userAvatar = uri
                    )
                }
                uploadUserAvatar(bitmap)
            },
            onError = {
                _uiState.value = UserInfoUiState.OnAvatarError
            }
        )
    }

    private fun setCode(code: String, country: String) {
        preferencesDatastoreManager.code = code
        _userInfoScreenState.update {
            it.copy(
                code = code,
                country = country
            )
        }
    }

    private fun validatePhone(phone: String) {
        validatePhone(
            phone,
            _userInfoScreenState.value.country
        ) { valid ->
            _userInfoScreenState.update {
                it.copy(
                    phone = phone,
                    phoneValid = valid
                )
            }
        }
    }

    private fun validateUserName(name: String) {
        validateName(name) { valid ->
            _userInfoScreenState.update {
                it.copy(
                    name =  name,
                    nameValid = valid
                )
            }
        }
    }

    private fun uploadUserAvatar(image: Bitmap) {
        uploadUserAvatar(image) {
            communicator.avatarServerPath = it
        }
    }

    private fun saveUserInfo() {
        networkExecutor {
            execute {
                userProfileRepository.saveUserInfo(
                    UserInfoEntity(
                        name = _userInfoScreenState.value.name,
                        phone = _userInfoScreenState.value.phone,
                        countryCode = _userInfoScreenState.value.code,
                        avatar = communicator.avatarServerPath,
                        schedule = communicator.schedule
                    )
                )
            }
            onSuccess {
                preferencesDatastoreManager.apply {
                    name = _userInfoScreenState.value.name
                    code = _userInfoScreenState.value.code
                    phone = _userInfoScreenState.value.phone
                }
                navigator.navigateOnMain(viewModelScope, UserProfileRoutes.UserLocation)
            }
            onError { error ->
                _uiState.value = UserInfoUiState.OnError(error)
            }
            onLoading { isLoading ->
                _userInfoScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}
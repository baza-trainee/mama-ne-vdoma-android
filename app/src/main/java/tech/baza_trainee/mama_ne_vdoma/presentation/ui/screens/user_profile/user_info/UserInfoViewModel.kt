package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
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
    private val phoneNumberUtil: PhoneNumberUtil,
    private val bitmapHelper: BitmapHelper
): ViewModel() {

    private val _userInfoScreenState = MutableStateFlow(UserInfoViewState())
    val userInfoScreenState: StateFlow<UserInfoViewState> = _userInfoScreenState.asStateFlow()

    private val _uiState = mutableStateOf<UserInfoUiState>(UserInfoUiState.Idle)
    val uiState: State<UserInfoUiState>
        get() = _uiState

    init {
        _userInfoScreenState.update {
            it.copy(
                name = communicator.name,
                nameValid = if (communicator.name.isEmpty()) ValidField.EMPTY else ValidField.VALID,
                code = communicator.code,
                phone = communicator.phone,
                phoneValid = if (communicator.phone.isEmpty()) ValidField.EMPTY else ValidField.VALID,
                userAvatar = communicator.userAvatar
            )
        }
        if (communicator.croppedImage != BitmapHelper.DEFAULT_BITMAP) {
            saveUserAvatar(communicator.croppedImage)
            communicator.croppedImage = BitmapHelper.DEFAULT_BITMAP
        }
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
        networkExecutor {
            execute {
                userProfileRepository.deleteUserAvatar()
            }
            onSuccess {
                communicator.apply {
                    uriForCrop = Uri.EMPTY
                    userAvatar = BitmapHelper.DEFAULT_BITMAP
                    avatar = null
                }
                _userInfoScreenState.update {
                    it.copy(
                        userAvatar = BitmapHelper.DEFAULT_BITMAP
                    )
                }
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

    private fun saveUserAvatar(image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val newImage = if (image.height > IMAGE_DIM)
                Bitmap.createScaledBitmap(image, IMAGE_DIM, IMAGE_DIM, true)
            else image
            val newImageSize = bitmapHelper.getSize(newImage)
            if (newImageSize < IMAGE_SIZE) {
                _userInfoScreenState.update {
                    it.copy(
                        userAvatar = newImage
                    )
                }
                uploadUserAvatar(newImage)
            } else {
                _uiState.value = UserInfoUiState.OnAvatarError
            }
        }
    }

    private fun setCode(code: String, country: String) {
        _userInfoScreenState.update {
            it.copy(
                code = code,
                country = country
            )
        }
    }

    private fun validatePhone(phone: String) {
        val phoneValid = try {
            val fullNumber = _userInfoScreenState.value.code + phone
            val phoneNumber = phoneNumberUtil.parse(fullNumber, _userInfoScreenState.value.country)
            if (phoneNumberUtil.isPossibleNumber(phoneNumber)) ValidField.VALID
            else ValidField.INVALID
        } catch (e: Exception) {
            ValidField.INVALID
        }
        _userInfoScreenState.update {
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

        _userInfoScreenState.update {
            it.copy(
                name =  name,
                nameValid = nameValid
            )
        }
    }

    private fun uploadUserAvatar(image: Bitmap) {
        networkExecutor<String> {
            execute {
                userProfileRepository.saveUserAvatar(image)
            }
            onSuccess {
                communicator.avatar = it
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

    private fun saveUserInfo() {
        networkExecutor {
            execute {
                userProfileRepository.saveUserInfo(
                    UserInfoEntity(
                        name = _userInfoScreenState.value.name,
                        phone = _userInfoScreenState.value.phone,
                        countryCode = _userInfoScreenState.value.code,
                        avatar = communicator.avatar,
                        schedule = communicator.schedule
                    )
                )
            }
            onSuccess {
                communicator.apply {
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

    companion object {

        private val NAME_LENGTH = 2..18
        private const val IMAGE_SIZE = 10 * 1024 * 1024
        private const val IMAGE_DIM = 512
    }
}
package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.decodeBase64
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class UserInfoViewModel(
    private val communicator: UserProfileCommunicator,
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository,
    private val phoneNumberUtil: PhoneNumberUtil,
    private val bitmapHelper: BitmapHelper
): ViewModel() {

    private val _userInfoScreenState = MutableStateFlow(UserInfoViewState())
    val userInfoScreenState: StateFlow<UserInfoViewState> = _userInfoScreenState.asStateFlow()

    private var uriForCrop: Uri = Uri.EMPTY

    init {
        if (communicator.name.isEmpty())
            getUserInfo()
        else
            _userInfoScreenState.update {
                it.copy(
                    name = communicator.name,
                    nameValid = ValidField.VALID,
                    code = communicator.code,
                    phone = communicator.phone,
                    phoneValid = ValidField.VALID,
                    userAvatar = communicator.userAvatar
                )
            }
    }

    fun handleUserInfoEvent(event: UserInfoEvent) {
        when(event) {
            is UserInfoEvent.SetImageToCrop -> setUriForCrop(event.uri)
            is UserInfoEvent.ValidateUserName -> validateUserName(event.name)
            is UserInfoEvent.ValidatePhone -> validatePhone(event.phone)
            is UserInfoEvent.SetCode -> setCode(event.code, event.country)
            UserInfoEvent.SaveInfo -> saveUserInfo()
            UserInfoEvent.ConsumeRequestError -> consumeUserInfoRequestError()
        }
    }

    fun getUserAvatarBitmap(): Bitmap {
        return bitmapHelper.bitmapFromUri(uriForCrop)
    }

    fun saveUserAvatar(image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val newImage = Bitmap.createScaledBitmap(image, 512, 512, true)
            val newImageSize = bitmapHelper.getSize(newImage)
            if (newImageSize < IMAGE_SIZE) {
                _userInfoScreenState.update {
                    it.copy(
                        userAvatar = image
                    )
                }
            } else {
                _userInfoScreenState.update {
                    it.copy(
                        avatarSizeError = triggered
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
                _userInfoScreenState.update {
                    it.copy(
                        name = entity?.name.orEmpty(),
                        phone = entity?.phone.orEmpty(),
                        code = entity?.countryCode.orEmpty(),
                        userAvatar = entity?.avatar.orEmpty().decodeBase64(),
                        nameValid = if (!entity?.name.isNullOrEmpty()) ValidField.VALID else ValidField.EMPTY,
                        phoneValid = if (!entity?.phone.isNullOrEmpty()) ValidField.VALID else ValidField.EMPTY,
                    )
                }
                getAddressFromLocation(
                    latLng = LatLng(
                        entity?.location?.coordinates?.get(1) ?: 0.00,
                        entity?.location?.coordinates?.get(0) ?: 0.00
                    )
                )
            }
            onError { error ->
                _userInfoScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
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

    private fun getAddressFromLocation(latLng: LatLng) {
        networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess {
                communicator.address = it.orEmpty()
            }
            onError { error ->
                _userInfoScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
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

    private fun setUriForCrop(uri: Uri) {
        uriForCrop = uri
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
        val nameValid = if (name.length in NAME_LENGTH && name.all { it.isLetter() || it.isDigit() }) ValidField.VALID
        else ValidField.INVALID
        _userInfoScreenState.update {
            it.copy(
                name =  name,
                nameValid = nameValid
            )
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
                        avatar = bitmapHelper.encodeToBase64(_userInfoScreenState.value.userAvatar)
                    )
                )
            }
            onSuccess {
                communicator.apply {
                    name = _userInfoScreenState.value.name
                    userAvatar = _userInfoScreenState.value.userAvatar
                }
                _userInfoScreenState.update {
                    it.copy(
                        requestSuccess = triggered
                    )
                }
            }
            onError { error ->
                _userInfoScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
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

    private fun consumeUserInfoRequestError() {
        _userInfoScreenState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }

    companion object {

        private val NAME_LENGTH = 2..18
        private const val IMAGE_SIZE = 250 * 1024
    }
}
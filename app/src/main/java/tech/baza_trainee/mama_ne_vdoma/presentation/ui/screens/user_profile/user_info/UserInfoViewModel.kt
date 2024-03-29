package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserProfileInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.CropImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.Communicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import java.time.DayOfWeek

class UserInfoViewModel(
    private val communicator: Communicator<SnapshotStateMap<DayOfWeek, DayPeriod>>,
    private val imageCommunicator: CropImageCommunicator,
    private val navigator: ScreenNavigator,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager,
    userProfileInteractor: UserProfileInteractor
): ViewModel(), UserProfileInteractor by userProfileInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(UserInfoViewState())
    val viewState: StateFlow<UserInfoViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<UserInfoUiState>(UserInfoUiState.Idle)
    val uiState: StateFlow<UserInfoUiState>
        get() = _uiState.asStateFlow()

    init {
        userProfileInteractor.apply {
            setUserProfileCoroutineScope(viewModelScope)
            setUserProfileNetworkListener(this@UserInfoViewModel)
        }

        _viewState.update {
            it.copy(
                name = preferencesDatastoreManager.name,
                nameValid = if (preferencesDatastoreManager.name.isEmpty()) ValidField.EMPTY else ValidField.VALID,
                code = preferencesDatastoreManager.code,
                phone = preferencesDatastoreManager.phone,
                phoneValid = if (preferencesDatastoreManager.phone.isEmpty()) ValidField.EMPTY else ValidField.VALID,
                userAvatar = preferencesDatastoreManager.avatarUri
            )
        }

        viewModelScope.launch {
            imageCommunicator.croppedImageFlow.collect(::saveUserAvatar)
        }
    }

    override fun onLoading(state: Boolean) {
        _viewState.update {
            it.copy(isLoading = state)
        }
    }

    override fun onError(error: String) {
        _uiState.update { UserInfoUiState.OnError(error) }
    }

    fun handleUserInfoEvent(event: UserInfoEvent) {
        when (event) {
            is UserInfoEvent.SetImageToCrop -> setUriForCrop(event.uri)
            is UserInfoEvent.ValidateUserName -> validateUserName(event.name)
            is UserInfoEvent.ValidatePhone -> validatePhone(event.phone)
            is UserInfoEvent.SetCode -> setCode(event.code, event.country)
            UserInfoEvent.SaveInfo -> uploadUserAvatar(_viewState.value.bitmapAvatar)
            UserInfoEvent.ResetUiState -> _uiState.update { UserInfoUiState.Idle }
            UserInfoEvent.OnEditPhoto -> navigator.navigate(UserProfileRoutes.ImageCrop)
            UserInfoEvent.OnDeletePhoto -> deleteUserAvatar()
            UserInfoEvent.OnBack -> navigator.navigate(UserProfileRoutes.FullProfile)
        }
    }

    private fun setUriForCrop(uri: Uri) {
        imageCommunicator.uriForCrop = uri
    }

    private fun deleteUserAvatar() {
        deleteUserAvatar {
            imageCommunicator.uriForCrop = Uri.EMPTY

            _viewState.update {
                it.copy(userAvatar = Uri.EMPTY)
            }
        }
    }

    private fun saveUserAvatar(image: Bitmap) {
        if (imageCommunicator.justCropped) {
            saveUserAvatar(
                avatar = image,
                onSuccess = { bitmap, uri ->
                    _viewState.update {
                        it.copy(
                            userAvatar = uri,
                            bitmapAvatar = bitmap
                        )
                    }
                    imageCommunicator.justCropped = false
                    imageCommunicator.setCroppedImage(null)
                },
                onError = {
                    _uiState.update { UserInfoUiState.OnAvatarError }
                }
            )
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
        validatePhone(
            phone,
            _viewState.value.country
        ) { valid ->
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
                    name = name,
                    nameValid = valid
                )
            }
        }
    }

    private fun uploadUserAvatar(image: Bitmap) {
        uploadUserAvatar(image) {
            saveUserInfo()
        }
    }

    private fun saveUserInfo() {
        updateParent(
            UserInfoEntity(
                name = _viewState.value.name,
                phone = _viewState.value.phone,
                countryCode = _viewState.value.code,
                avatar = preferencesDatastoreManager.avatar,
                schedule = communicator.dataFlow.value
            )
        ) {
            navigator.navigate(UserProfileRoutes.UserLocation)
        }
    }
}
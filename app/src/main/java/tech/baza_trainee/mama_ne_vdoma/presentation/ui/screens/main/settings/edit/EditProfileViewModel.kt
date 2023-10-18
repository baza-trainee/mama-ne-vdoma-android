package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.ifNullOrEmpty
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class EditProfileViewModel(
    private val navigator: ScreenNavigator,
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val locationRepository: LocationRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(EditProfileViewState())
    val viewState: StateFlow<EditProfileViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<EditProfileUiState>(EditProfileUiState.Idle)
    val uiState: State<EditProfileUiState>
        get() = _uiState

    init {
        getUserInfo()
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
            EditProfileEvent.GetLocationFromAddress -> TODO()
            EditProfileEvent.OnDeletePhoto -> TODO()
            EditProfileEvent.OnEditPhoto -> TODO()
            is EditProfileEvent.OnMapClick -> TODO()
            EditProfileEvent.RequestUserLocation -> TODO()
            is EditProfileEvent.SetCode -> TODO()
            is EditProfileEvent.SetImageToCrop -> TODO()
            is EditProfileEvent.UpdatePolicyCheck -> TODO()
            is EditProfileEvent.UpdateUserAddress -> TODO()
            is EditProfileEvent.ValidateEmail -> TODO()
            is EditProfileEvent.ValidatePassword -> TODO()
            is EditProfileEvent.ValidatePhone -> TODO()
            is EditProfileEvent.ValidateUserName -> TODO()
            EditProfileEvent.VerifyEmail -> TODO()
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
                    getAddressFromLocation(
                        latLng = LatLng(
                            entity.location.coordinates[1],
                            entity.location.coordinates[0]
                        )
                    )
//                    preferencesDatastoreManager.apply {
//                        latitude = entity.location.coordinates[1]
//                        longitude = entity.location.coordinates[0]
//                    }
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
}
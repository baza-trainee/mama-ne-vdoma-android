package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main_profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserProfileInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.LoginRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.common.EditProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

class ProfileSettingsViewModel(
    private val mainNavigator: ScreenNavigator,
    private val navigator: PageNavigator,
    private val communicator: EditProfileCommunicator,
    private val userProfileInteractor: UserProfileInteractor,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel(), UserProfileInteractor by userProfileInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(ProfileSettingsViewState())
    val viewState: StateFlow<ProfileSettingsViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        userProfileInteractor.apply {
            setUserProfileCoroutineScope(viewModelScope)
            setUserProfileNetworkListener(this@ProfileSettingsViewModel)
        }

        viewModelScope.launch {
            communicator.profileChanged.collect { success ->
                if (success) {
                    getUserInfo()
                    getChildren()
                    communicator.setProfileChanged(false)
                } else {
                    _viewState.update {
                        it.copy(
                            name = preferencesDatastoreManager.name,
                            address = preferencesDatastoreManager.address,
                            email = preferencesDatastoreManager.email,
                            phone = preferencesDatastoreManager.phone,
                            code = preferencesDatastoreManager.code,
                            sendEmails = preferencesDatastoreManager.sendEmail
                        )
                    }
                    getChildren()
                }
            }
        }

        viewModelScope.launch {
            preferencesDatastoreManager.userPreferencesFlow.collect { prefs ->
                _viewState.update {
                    it.copy(avatar = prefs.avatarUri)
                }
            }
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
        _uiState.value = RequestState.OnError(error)
    }

    fun handleEvent(event: ProfileSettingsEvent) {
        when (event) {
            ProfileSettingsEvent.OnBack -> navigator.goToPrevious()
            ProfileSettingsEvent.ResetUiState -> _uiState.value = RequestState.Idle
            is ProfileSettingsEvent.UpdatePolicyCheck -> updatePolicyCheck(event.isChecked)
            ProfileSettingsEvent.EditProfile -> navigator.navigate(SettingsScreenRoutes.EditProfile)
            ProfileSettingsEvent.LogOut -> {
                preferencesDatastoreManager.clearData()
                mainNavigator.navigate(LoginRoutes.Login)
            }

            ProfileSettingsEvent.ToggleEmail -> getUserInfo()
            ProfileSettingsEvent.DeleteUser -> deleteUser()
            ProfileSettingsEvent.EditCredentials ->
                navigator.navigate(SettingsScreenRoutes.EditCredentials)
        }
    }

    private fun deleteUser() {
        deleteUser {
            mainNavigator.navigate(Graphs.CreateUser)
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
        getUserInfo {
            val entity = UserInfoEntity(
                name = it.name,
                avatar = it.avatar,
                countryCode = it.countryCode,
                phone = it.phone,
                schedule = it.schedule,
                sendingEmails = !_viewState.value.sendEmails
            )
            saveUserInfo(entity)
        }
    }

    private fun saveUserInfo(user: UserInfoEntity) {
        updateParent(user) {
            preferencesDatastoreManager.sendEmail = !_viewState.value.sendEmails
            _viewState.update {
                it.copy(
                    sendEmails = !it.sendEmails
                )
            }
        }
    }

    private fun updatePolicyCheck(isChecked: Boolean) {
        _viewState.update {
            it.copy(
                isPolicyChecked = isChecked
            )
        }
    }
}
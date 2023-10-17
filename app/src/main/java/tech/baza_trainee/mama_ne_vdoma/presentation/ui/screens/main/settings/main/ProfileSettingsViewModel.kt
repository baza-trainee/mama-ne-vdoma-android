package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.data.interceptors.AuthInterceptor
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.LoginRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class ProfileSettingsViewModel(
    private val mainNavigator: ScreenNavigator,
    private val navigator: PageNavigator,
    private val userProfileRepository: UserProfileRepository,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(ProfileSettingsViewState())
    val viewState: StateFlow<ProfileSettingsViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        _viewState.update {
            it.copy(
                name = preferencesDatastoreManager.name,
                address = preferencesDatastoreManager.address,
                email = preferencesDatastoreManager.email,
                phone = preferencesDatastoreManager.phone,
                code = preferencesDatastoreManager.code,
                avatar = Uri.parse(preferencesDatastoreManager.avatar)
            )
        }
        getChildren()
    }

    fun handleEvent(event: ProfileSettingsEvent) {
        when (event) {
            ProfileSettingsEvent.OnBack -> navigator.goToPrevious()
            ProfileSettingsEvent.ResetUiState -> _uiState.value = RequestState.Idle
            ProfileSettingsEvent.EditProfile -> Unit
            ProfileSettingsEvent.LogOut -> {
                AuthInterceptor.AUTH_TOKEN = AuthInterceptor.EMPTY_TOKEN
                mainNavigator.navigate(LoginRoutes.Login)
            }

            ProfileSettingsEvent.ToggleEmail -> {
                _viewState.update {
                    it.copy(
                        sendEmails = !it.sendEmails
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
                _uiState.value = RequestState.OnError(error)
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
}
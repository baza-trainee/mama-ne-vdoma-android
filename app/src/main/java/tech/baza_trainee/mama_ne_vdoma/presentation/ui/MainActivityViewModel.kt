package tech.baza_trainee.mama_ne_vdoma.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class MainActivityViewModel(
    private val userProfileRepository: UserProfileRepository
): ViewModel() {

    private val _canAuthenticate = MutableStateFlow(false)
    val canAuthenticate: StateFlow<Boolean> = _canAuthenticate.asStateFlow()

    fun checkAuthorization() {
        viewModelScope.networkExecutor {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { _canAuthenticate.value = true }
        }
    }
}
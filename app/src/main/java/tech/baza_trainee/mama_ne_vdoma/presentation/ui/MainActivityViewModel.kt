package tech.baza_trainee.mama_ne_vdoma.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.MAIN_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onErrorWithCode
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.net.HttpURLConnection

class MainActivityViewModel(
    private val navigator: ScreenNavigator,
    private val userProfileRepository: UserProfileRepository
): ViewModel() {

    fun checkAuthorization() {
        viewModelScope.networkExecutor {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { navigator.navigate(HostScreenRoutes.Host.getDestination(MAIN_PAGE)) }
            onErrorWithCode { _, code ->
                if (code == HttpURLConnection.HTTP_UNAUTHORIZED)
                    navigator.navigate(Graphs.Login)
            }
        }
    }
}
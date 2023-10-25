package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import android.net.Uri
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CommonHostRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes

data class HostViewState(
    val currentRoute: CommonHostRoute = MainScreenRoutes.Main,
    val myRequests: Int = 0,
    val joinRequests: Int = 0,
    val avatar: Uri = Uri.EMPTY,
    val isLoading: Boolean = false
)

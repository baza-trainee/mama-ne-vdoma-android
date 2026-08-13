package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.MAIN_PAGE

sealed interface MainScreenRoutes : CommonHostRoute {

    override val page: Int get() = MAIN_PAGE

    @Serializable
    data object Main : MainScreenRoutes {
        override val title: Int get() = R.string.title_main_page
    }

    @Serializable
    data object Notifications : MainScreenRoutes {
        override val title: Int get() = R.string.title_notifications
    }
}

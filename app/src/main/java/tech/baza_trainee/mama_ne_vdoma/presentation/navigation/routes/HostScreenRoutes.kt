package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.NO_PAGE

sealed interface HostScreenRoutes : CommonRoute {

    /**
     * [page] defaults to [NO_PAGE] so that entering the graph without a target page keeps the tab
     * the host is already on.
     */
    @Serializable
    data class Host(val page: Int = NO_PAGE) : HostScreenRoutes
}

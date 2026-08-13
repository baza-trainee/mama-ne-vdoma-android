package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable

sealed interface StartRoutes : CommonRoute {

    @Serializable
    data object Start : StartRoutes

    @Serializable
    data object Info : StartRoutes
}

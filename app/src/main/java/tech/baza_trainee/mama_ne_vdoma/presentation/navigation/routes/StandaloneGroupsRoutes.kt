package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable

sealed interface StandaloneGroupsRoutes : CommonRoute {

    @Serializable
    data object SetArea : StandaloneGroupsRoutes

    @Serializable
    data object GroupsFound : StandaloneGroupsRoutes

    @Serializable
    data object GroupImageCrop : StandaloneGroupsRoutes

    @Serializable
    data object CreateGroup : StandaloneGroupsRoutes

    @Serializable
    data class ChooseChild(val isForSearch: Boolean = false) : StandaloneGroupsRoutes
}

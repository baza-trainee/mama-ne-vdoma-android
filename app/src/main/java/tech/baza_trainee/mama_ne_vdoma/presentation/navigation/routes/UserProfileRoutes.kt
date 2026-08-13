package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable

sealed interface UserProfileRoutes : CommonRoute {

    @Serializable
    data object UserInfo : UserProfileRoutes

    @Serializable
    data object ImageCrop : UserProfileRoutes

    @Serializable
    data object UserLocation : UserProfileRoutes

    @Serializable
    data object ChildInfo : UserProfileRoutes

    @Serializable
    data object ChildSchedule : UserProfileRoutes

    @Serializable
    data object ParentSchedule : UserProfileRoutes

    @Serializable
    data object FullProfile : UserProfileRoutes

    @Serializable
    data class UserCreateSuccess(val name: String) : UserProfileRoutes
}

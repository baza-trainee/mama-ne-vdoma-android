package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable

sealed interface CreateUserRoute : CommonRoute {

    @Serializable
    data object CreateUser : CreateUserRoute

    @Serializable
    data object VerifyEmail : CreateUserRoute
}

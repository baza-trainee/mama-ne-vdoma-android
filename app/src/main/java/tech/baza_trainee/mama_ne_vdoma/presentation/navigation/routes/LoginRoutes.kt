package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable

sealed interface LoginRoutes : CommonRoute {

    @Serializable
    data object Login : LoginRoutes

    @Serializable
    data object RestorePassword : LoginRoutes

    @Serializable
    data object RestoreSuccess : LoginRoutes

    @Serializable
    data object VerifyEmail : LoginRoutes

    @Serializable
    data object NewPassword : LoginRoutes

    @Serializable
    data class EmailConfirm(
        val email: String,
        val password: String
    ) : LoginRoutes
}

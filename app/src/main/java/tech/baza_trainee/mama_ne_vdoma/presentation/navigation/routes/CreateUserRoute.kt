package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class CreateUserRoute(override val route: String): CommonRoute(route) {
    data object CreateUser: CreateUserRoute("create_user_screen")
    data object VerifyEmail : CreateUserRoute("email_verify_create_screen")
}
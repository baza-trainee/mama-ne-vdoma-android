package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class CreateUserRoute(val route: String) {
    object CreateUser: CreateUserRoute("create_user_screen")
    object VerifyEmail: CreateUserRoute("verify_email_screen")
}
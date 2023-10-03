package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class LoginRoutes(val route: String) {
    object Login: LoginRoutes("login_screen")
    object RestorePassword: LoginRoutes("restore_password_screen")
    object EmailConfirm: LoginRoutes("email_confirm_screen")
    object RestoreSuccess: LoginRoutes("restore_success_screen")
    object NewPassword: LoginRoutes("new_password_screen")
}
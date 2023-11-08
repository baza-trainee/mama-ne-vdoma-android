package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class LoginRoutes(override val route: String): CommonRoute(route) {
    data object Login: LoginRoutes("login_screen")
    data object RestorePassword: LoginRoutes("restore_password_screen")
    data object RestoreSuccess: LoginRoutes("restore_success_screen")
    data object VerifyEmail : LoginRoutes("email_verify_restore_screen")
    data object  NewPassword : LoginRoutes("new_password_screen")

    class EmailConfirm : LoginRoutes(ROUTE) {

        data class EmailConfirmArgs (
            val email: String,
            val password: String
        )

        companion object {

            const val ROUTE = "$BASE_ROUTE_EMAIL?$EMAIL={$EMAIL},$PASSWORD={$PASSWORD}"

            val argumentList: MutableList<NamedNavArgument>
                get() = mutableListOf(
                    navArgument(EMAIL) {
                        type = NavType.StringType
                    },
                    navArgument(PASSWORD) {
                        type = NavType.StringType
                    }
                )

            fun parseArguments(backStackEntry: NavBackStackEntry): EmailConfirmArgs {
                return EmailConfirmArgs(
                    email = backStackEntry.arguments?.getString(EMAIL) ?: "",
                    password = backStackEntry.arguments?.getString(PASSWORD) ?: ""
                )
            }

            fun getDestination(email: String, password: String): CommonRoute {
                return CommonRoute(
                    "$BASE_ROUTE_EMAIL?" +
                            "$EMAIL=$email," +
                            "$PASSWORD=$password" +
                            ""
                )
            }
        }
    }

    companion object {

        private const val BASE_ROUTE_EMAIL = "email_confirm_screen"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}
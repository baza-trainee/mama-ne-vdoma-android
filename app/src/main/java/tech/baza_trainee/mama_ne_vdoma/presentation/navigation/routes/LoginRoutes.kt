package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class LoginRoutes(override val route: String): CommonRoute(route) {
    data object Login: LoginRoutes("login_screen")
    data object RestorePassword: LoginRoutes("restore_password_screen")
    data object RestoreSuccess: LoginRoutes("restore_success_screen")

    class VerifyEmail : LoginRoutes(ROUTE) {

        data class VerifyEmailArgs (
            val email: String,
            val password: String
        )

        companion object {

            const val ROUTE = "$BASE_ROUTE_VERIFY?$EMAIL={$EMAIL},$PASSWORD={$PASSWORD}"

            val argumentList: MutableList<NamedNavArgument>
                get() = mutableListOf(
                    navArgument(EMAIL) {
                        type = NavType.StringType
                    },
                    navArgument(PASSWORD) {
                        type = NavType.StringType
                    }
                )

            fun parseArguments(backStackEntry: NavBackStackEntry): VerifyEmailArgs {
                return VerifyEmailArgs(
                    email = backStackEntry.arguments?.getString(EMAIL) ?: "",
                    password = backStackEntry.arguments?.getString(PASSWORD) ?: ""
                )
            }

            fun getDestination(email: String, password: String): CommonRoute {
                return CommonRoute(
                    "$BASE_ROUTE_VERIFY?" +
                        "$EMAIL=$email," +
                        "$PASSWORD=$password" +
                        ""
                )
            }
        }
    }

    class NewPassword : LoginRoutes(ROUTE) {

        data class NewPassArgs (
            val email: String,
            val otp: String
        )

        companion object {

            const val ROUTE = "$BASE_ROUTE_PASSWORD?$EMAIL={$EMAIL},$OTP={$OTP}"

            val argumentList: MutableList<NamedNavArgument>
                get() = mutableListOf(
                    navArgument(EMAIL) {
                        type = NavType.StringType
                    },
                    navArgument(OTP) {
                        type = NavType.StringType
                    }
                )

            fun parseArguments(backStackEntry: NavBackStackEntry): NewPassArgs {
                return NewPassArgs(
                    email = backStackEntry.arguments?.getString(EMAIL) ?: "",
                    otp = backStackEntry.arguments?.getString(OTP) ?: ""
                )
            }

            fun getDestination(email: String, otp: String): CommonRoute {
                return CommonRoute(
                    "$BASE_ROUTE_PASSWORD?" +
                        "$EMAIL=$email," +
                        "$OTP=$otp" +
                        ""
                )
            }
        }
    }

    class EmailConfirm : LoginRoutes(ROUTE) {

        data class EmailConfirmArgs (
            val email: String
        )

        companion object {

            const val ROUTE = "$BASE_ROUTE_EMAIL?$EMAIL={$EMAIL}"

            val argumentList: MutableList<NamedNavArgument>
                get() = mutableListOf(
                    navArgument(EMAIL) {
                        type = NavType.StringType
                    }
                )

            fun parseArguments(backStackEntry: NavBackStackEntry): EmailConfirmArgs {
                return EmailConfirmArgs(
                    email = backStackEntry.arguments?.getString(EMAIL) ?: ""
                )
            }

            fun getDestination(email: String): CommonRoute {
                return CommonRoute(
                    "$BASE_ROUTE_EMAIL?" +
                        "$EMAIL=$email" +
                        ""
                )
            }
        }
    }

    companion object {

        private const val BASE_ROUTE_VERIFY = "email_verify_restore_screen"
        private const val BASE_ROUTE_PASSWORD = "new_password_screen"
        private const val BASE_ROUTE_EMAIL = "email_confirm_screen"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val OTP = "otp"
    }
}
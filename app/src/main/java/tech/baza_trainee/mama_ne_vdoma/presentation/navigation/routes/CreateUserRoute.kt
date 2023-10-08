package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class CreateUserRoute(val route: String) {
    object CreateUser: CreateUserRoute("create_user_screen")


    class VerifyEmail : CreateUserRoute(ROUTE) {

        data class VerifyEmailArgs (
            val email: String,
            val password: String
        )

        companion object {

            const val ROUTE = "${BASE_ROUTE}?${EMAIL}={${EMAIL}},${PASSWORD}={${PASSWORD}}"

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
                    "${BASE_ROUTE}?" +
                        "${EMAIL}=$email," +
                        "${PASSWORD}=$password" +
                        ""
                )
            }
        }
    }

    companion object {

        private const val BASE_ROUTE = "email_verify_create_screen"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}
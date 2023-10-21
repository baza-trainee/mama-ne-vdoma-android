package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class HostScreenRoutes(override val route: String): CommonRoute(route) {
    class Host: HostScreenRoutes(ROUTE) {

        data class HostArgs (
            val page: Int
        )

        companion object {

            const val ROUTE = "${BASE_ROUTE}?${PAGE}={${PAGE}}"

            val argumentList: MutableList<NamedNavArgument>
                get() = mutableListOf(
                    navArgument(PAGE) {
                        type = NavType.IntType
                    }
                )

            fun parseArguments(backStackEntry: NavBackStackEntry): HostArgs {
                return HostArgs(
                    page = backStackEntry.arguments?.getInt(PAGE) ?: -1,
                )
            }

            fun getDestination(page: Int): CommonRoute {
                return CommonRoute(
                    "${BASE_ROUTE}?" +
                            "${PAGE}=$page" +
                            ""
                )
            }
        }
    }

    companion object {

        private const val BASE_ROUTE = "host_screen"
        private const val PAGE = "page"
    }
}
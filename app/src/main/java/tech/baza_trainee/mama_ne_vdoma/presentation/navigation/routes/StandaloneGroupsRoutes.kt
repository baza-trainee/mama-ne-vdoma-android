package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class StandaloneGroupsRoutes(override val route: String): CommonRoute(route) {
    data object SetArea: StandaloneGroupsRoutes("set_area_screen")
    data object GroupsFound: StandaloneGroupsRoutes("groups_found_screen")
    data object GroupImageCrop: StandaloneGroupsRoutes("group_avatar_crop_screen")
    data object CreateGroup: StandaloneGroupsRoutes("create_group_screen")

    class ChooseChild: StandaloneGroupsRoutes(ROUTE) {

        data class ChooseChildArgs (
            val isForSearch: Boolean
        )

        companion object {

            const val ROUTE = "${BASE_ROUTE}?${IS_FOR_SEARCH}={${IS_FOR_SEARCH}}"

            val argumentList: MutableList<NamedNavArgument>
                get() = mutableListOf(
                    navArgument(IS_FOR_SEARCH) {
                        type = NavType.BoolType
                    }
                )

            fun parseArguments(backStackEntry: NavBackStackEntry): ChooseChildArgs {
                return ChooseChildArgs(
                    isForSearch = backStackEntry.arguments?.getBoolean(IS_FOR_SEARCH) ?: false
                )
            }

            fun getDestination(isForSearch: Boolean): CommonRoute {
                return CommonRoute(
                    "${BASE_ROUTE}?" +
                            "${IS_FOR_SEARCH}=$isForSearch" +
                            ""
                )
            }
        }
    }

    companion object {

        private const val BASE_ROUTE = "choose_child_screen"
        private const val IS_FOR_SEARCH = "is_for_search"
    }
}

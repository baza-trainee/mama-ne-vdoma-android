package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GROUPS_PAGE

sealed class GroupsScreenRoutes(override val route: String): CommonHostRoute(route, GROUPS_PAGE, "Групи") {
    data object Groups: GroupsScreenRoutes(route = "groups_screen")
    data object ChooseChild: GroupsScreenRoutes("choose_child_screen")
    data object ImageCrop: GroupsScreenRoutes("user_crop_screen")


    class CreateGroup: GroupsScreenRoutes(ROUTE) {
        data class CreateGroupArgs (
            val child: String
        )

        companion object {

            const val ROUTE = "$BASE_ROUTE_CREATE_GROUP?$CHILD={$CHILD}"

            val argumentList: MutableList<NamedNavArgument>
                get() = mutableListOf(
                    navArgument(CHILD) {
                        type = NavType.StringType
                    }
                )

            fun parseArguments(backStackEntry: NavBackStackEntry): CreateGroupArgs {
                return CreateGroupArgs(
                    child = backStackEntry.arguments?.getString(CHILD) ?: ""
                )
            }

            fun getDestination(child: String): CommonHostRoute {
                return CommonHostRoute(
                    route = "$BASE_ROUTE_CREATE_GROUP?" +
                            "$CHILD=$child" +
                            "",
                    GROUPS_PAGE,
                    "Групи"
                )
            }
        }
    }

    companion object {

        private const val BASE_ROUTE_CREATE_GROUP = "create_group_screen"
        private const val CHILD = "child"
    }
}
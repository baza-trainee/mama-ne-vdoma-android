package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class GroupsScreenRoutes(val route: String): CommonRoute(route) {
    data object Groups: GroupsScreenRoutes("groups_screen")
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

            fun getDestination(child: String): CommonRoute {
                return CommonRoute(
                    "$BASE_ROUTE_CREATE_GROUP?" +
                            "$CHILD=$child" +
                            ""
                )
            }
        }
    }

    companion object {

        private const val BASE_ROUTE_CREATE_GROUP = "create_group_screen"
        private const val CHILD = "child"
    }
}
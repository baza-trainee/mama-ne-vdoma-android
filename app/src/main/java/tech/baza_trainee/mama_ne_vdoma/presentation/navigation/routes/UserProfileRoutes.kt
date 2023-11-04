package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class UserProfileRoutes(override val route: String): CommonRoute(route) {

    data object UserInfo: UserProfileRoutes("user_info_screen")
    data object ImageCrop: UserProfileRoutes("user_crop_screen")
    data object UserLocation: UserProfileRoutes("user_location_screen")
    data object ChildInfo: UserProfileRoutes("child_info_screen")
    data object ChildSchedule: UserProfileRoutes("child_schedule_screen")

//    data object ChildrenInfo: UserProfileRoutes("children_info_screen")

    data object ParentSchedule : UserProfileRoutes("parent_schedule_screen")
    data object FullProfile : UserProfileRoutes("full_profile_screen")
    class UserCreateSuccess : CommonRoute(ROUTE){

        data class SuccessArgs(
            val name: String
        )

        companion object {

            const val ROUTE =
                "${BASE_ROUTE}?${NAME}={${NAME}}"

            val argumentList: MutableList<NamedNavArgument>
                get() = mutableListOf(
                    navArgument(NAME) {
                        type = NavType.StringType
                    }
                )

            fun parseArguments(backStackEntry: NavBackStackEntry): SuccessArgs {
                return SuccessArgs(
                    name = backStackEntry.arguments?.getString(NAME) ?: ""
                )
            }

            fun getDestination(name: String): CommonRoute {
                return CommonRoute(
                    "${BASE_ROUTE}?" +
                            "${NAME}=$name" +
                            ""
                )
            }
        }
    }

    companion object {

        private const val BASE_ROUTE = "user_create_success_screen"
        private const val NAME = "name"
    }
}
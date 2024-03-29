package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.GROUPS_PAGE

sealed class GroupsScreenRoutes(
    override val route: String,
    @StringRes override val title: Int
): CommonHostRoute(route, GROUPS_PAGE, R.string.title_groups) {
    data object Groups: GroupsScreenRoutes(route = "groups_screen", R.string.title_groups)
    data object UpdateGroup: GroupsScreenRoutes(route = "update_group_screen", R.string.title_edit_group)
    data object UpdateGroupAvatar: GroupsScreenRoutes(route = "update_group_avatar_screen", R.string.title_edit_group_avatar)

    class RateUser: GroupsScreenRoutes(route = ROUTE, -1) {

        data class RateUserArgs (
            val userId: String
        )

        companion object {

            const val ROUTE = "${BASE_ROUTE_RATE}?${USER_ID}={${USER_ID}}"

            val argumentList: MutableList<NamedNavArgument>
                get() = mutableListOf(
                    navArgument(USER_ID) {
                        type = NavType.StringType
                    }
                )

            fun parseArguments(backStackEntry: NavBackStackEntry): RateUserArgs {
                return RateUserArgs(
                    userId = backStackEntry.arguments?.getString(USER_ID).orEmpty()
                )
            }

            fun getDestination(userId: String): CommonHostRoute {
                return CommonHostRoute(
                    route = "${BASE_ROUTE_RATE}?" +
                            "${USER_ID}=$userId" +
                            "",
                    page = GROUPS_PAGE,
                    title = R.string.title_review_user
                )
            }
        }
    }

    class ViewReviews: GroupsScreenRoutes(route = ROUTE, -1) {

        data class ViewReviewsArgs (
            val userId: String
        )

        companion object {

            const val ROUTE = "${BASE_ROUTE_VIEW}?${USER_ID}={${USER_ID}}"

            val argumentList: MutableList<NamedNavArgument>
                get() = mutableListOf(
                    navArgument(USER_ID) {
                        type = NavType.StringType
                    }
                )

            fun parseArguments(backStackEntry: NavBackStackEntry): ViewReviewsArgs {
                return ViewReviewsArgs(
                    userId = backStackEntry.arguments?.getString(USER_ID).orEmpty()
                )
            }

            fun getDestination(userId: String): CommonHostRoute {
                return CommonHostRoute(
                    route = "${BASE_ROUTE_VIEW}?" +
                            "${USER_ID}=$userId" +
                            "",
                    page = GROUPS_PAGE,
                    title = R.string.title_reviews
                )
            }
        }
    }

    companion object {

        private const val BASE_ROUTE_RATE = "rate_user"
        private const val BASE_ROUTE_VIEW = "view_reviews"
        private const val USER_ID = "user_id"
    }
}
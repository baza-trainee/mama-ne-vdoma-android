package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class UserProfileRoutes(override val route: String): CommonRoute(route) {

    data object UserInfo: UserProfileRoutes("user_info_screen")
    data object ImageCrop: UserProfileRoutes("user_crop_screen")
    data object UserLocation: UserProfileRoutes("user_location_screen")
    data object ChildInfo: UserProfileRoutes("child_info_screen")
    data object ChildSchedule: UserProfileRoutes("child_schedule_screen")

//    data object ChildrenInfo: UserProfileRoutes("children_info_screen")

    data object ParentSchedule: UserProfileRoutes("parent_schedule_screen")
    data object FullProfile: UserProfileRoutes("full_profile_screen")
}
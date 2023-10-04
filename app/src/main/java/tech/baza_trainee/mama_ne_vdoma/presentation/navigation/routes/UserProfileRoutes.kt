package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class UserProfileRoutes(val route: String) {

    object UserInfo: UserProfileRoutes("user_info_screen")
    object ImageCrop: UserProfileRoutes("image_crop_screen")
    object UserLocation: UserProfileRoutes("user_location_screen")
    object ChildInfo: UserProfileRoutes("child_info_screen")
    object ChildSchedule: UserProfileRoutes("child_schedule_screen")
    object ChildrenInfo: UserProfileRoutes("children_info_screen")
    object ParentSchedule: UserProfileRoutes("parent_schedule_screen")
    object FullProfile: UserProfileRoutes("full_profile_screen")
}
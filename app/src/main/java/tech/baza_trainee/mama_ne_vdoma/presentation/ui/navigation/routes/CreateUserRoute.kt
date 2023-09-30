package tech.baza_trainee.mama_ne_vdoma.presentation.ui.navigation.routes

sealed class CreateUserRoute(val route: String) {
    object CreateUser: CreateUserRoute("create_user_screen")
    object UserInfo: CreateUserRoute("user_info_screen")
    object ImageCrop: CreateUserRoute("image_crop_screen")
    object UserLocation: CreateUserRoute("user_location_screen")
    object ChildInfo: CreateUserRoute("child_info_screen/{$CHILD_ID}")
    object ChildSchedule: CreateUserRoute("child_schedule_screen/{$CHILD_ID}")
    object ChildrenInfo: CreateUserRoute("children_info_screen")
    object ParentSchedule: CreateUserRoute("parent_schedule_screen")

    companion object {

        const val CHILD_ID = "childId"
    }
}
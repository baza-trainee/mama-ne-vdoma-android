package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SETTINGS_PAGE

sealed class SettingsScreenRoutes(override val route: String, override val title: String): CommonHostRoute(route, SETTINGS_PAGE, title) {
    data object Settings: SettingsScreenRoutes("settings_screen", "Мій акаунт")
    data object EditProfile: SettingsScreenRoutes("edit_profile_screen", "Редагування акаунту")
    data object EditProfilePhoto: SettingsScreenRoutes("edit_profile_photo_screen", "Редагування акаунту")
    data object VerifyNewEmail: SettingsScreenRoutes("verify_new_email_screen", "Веріфікація нових даних")
    data object ChildInfo: SettingsScreenRoutes("add_child_screen", "Розкажіть про свою дитину")
    data object ChildSchedule: SettingsScreenRoutes("add_child_schedule_screen", "Вкажіть, коли потрібно доглядати дитину")
    data object EditCredentials: SettingsScreenRoutes("edit_credentials_screen", "Змініть свої дані для авторизації")
    data object ChangePasswordConfirm: SettingsScreenRoutes("change_password_confirm_screen", "Падтвердження зміни пароля")
}
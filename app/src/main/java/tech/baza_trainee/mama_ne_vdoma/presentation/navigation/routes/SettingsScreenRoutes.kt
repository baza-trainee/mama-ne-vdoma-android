package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SETTINGS_PAGE

sealed interface SettingsScreenRoutes : CommonHostRoute {

    override val page: Int get() = SETTINGS_PAGE

    @Serializable
    data object Settings : SettingsScreenRoutes {
        override val title: Int get() = R.string.title_user_profile
    }

    @Serializable
    data object EditProfile : SettingsScreenRoutes {
        override val title: Int get() = R.string.title_edit_user_profile
    }

    @Serializable
    data object EditProfilePhoto : SettingsScreenRoutes {
        override val title: Int get() = R.string.title_edit_user_profile
    }

    @Serializable
    data object VerifyNewEmail : SettingsScreenRoutes {
        override val title: Int get() = R.string.title_verify_user_data
    }

    @Serializable
    data object ChildInfo : SettingsScreenRoutes {
        override val title: Int get() = R.string.title_add_child_data
    }

    @Serializable
    data object ChildSchedule : SettingsScreenRoutes {
        override val title: Int get() = R.string.title_add_child_schedule
    }

    @Serializable
    data object EditCredentials : SettingsScreenRoutes {
        override val title: Int get() = R.string.title_change_credentials
    }

    @Serializable
    data object ChangePasswordConfirm : SettingsScreenRoutes {
        override val title: Int get() = R.string.title_new_password_confirm
    }
}

package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.GROUPS_PAGE

sealed interface GroupsScreenRoutes : CommonHostRoute {

    override val page: Int get() = GROUPS_PAGE

    @Serializable
    data object Groups : GroupsScreenRoutes {
        override val title: Int get() = R.string.title_groups
    }

    @Serializable
    data object UpdateGroup : GroupsScreenRoutes {
        override val title: Int get() = R.string.title_edit_group
    }

    @Serializable
    data object UpdateGroupAvatar : GroupsScreenRoutes {
        override val title: Int get() = R.string.title_edit_group_avatar
    }

    @Serializable
    data class RateUser(val userId: String) : GroupsScreenRoutes {
        override val title: Int get() = R.string.title_review_user
    }

    @Serializable
    data class ViewReviews(val userId: String) : GroupsScreenRoutes {
        override val title: Int get() = R.string.title_reviews
    }
}

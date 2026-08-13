package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SEARCH_PAGE

sealed interface SearchScreenRoutes : CommonHostRoute {

    override val page: Int get() = SEARCH_PAGE
    override val title: Int get() = R.string.title_search

    @Serializable
    data object SearchUser : SearchScreenRoutes

    @Serializable
    data object SearchResults : SearchScreenRoutes
}

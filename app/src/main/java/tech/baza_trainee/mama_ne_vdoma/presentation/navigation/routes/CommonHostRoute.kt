package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import androidx.annotation.StringRes

open class CommonHostRoute(
    override val route: String,
    val page: Int,
    @StringRes open val title: Int
): CommonRoute(route)
package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import androidx.annotation.StringRes

/**
 * Marker for every navigation destination. Implementations are @Serializable so that they can be
 * used directly as type-safe routes.
 */
interface CommonRoute

/**
 * Destination shown inside the host, which owns a bottom navigation page and a toolbar title.
 * Both are constants of the destination rather than navigation arguments, so they stay out of the
 * serialized route.
 */
interface CommonHostRoute : CommonRoute {
    val page: Int

    @get:StringRes
    val title: Int
}

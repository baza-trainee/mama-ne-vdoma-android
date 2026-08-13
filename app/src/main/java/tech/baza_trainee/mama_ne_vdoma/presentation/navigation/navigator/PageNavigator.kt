package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator

import android.util.Log
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CommonHostRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CommonRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.NO_PAGE
import java.util.Deque
import java.util.LinkedList

interface PageNavigator: ScreenNavigator {

    val routesFlow: StateFlow<CommonHostRoute>

    fun goToPrevious()

    fun getCurrentRoute(): CommonHostRoute
}

/**
 * Placeholder emitted before the host navigates anywhere. Its [page] is [NO_PAGE] so that
 * collectors can tell it apart from a real destination.
 */
private data object NoRoute : CommonHostRoute {
    override val page = NO_PAGE
    override val title = 0
}

class PageNavigatorImpl: PageNavigator {

    override val routesFlow: StateFlow<CommonHostRoute>
        get() = _routesFlow.asStateFlow()
    private val _routesFlow = MutableStateFlow<CommonHostRoute>(NoRoute)

    private val routesQueue: Deque<CommonHostRoute> = LinkedList()

    private var goBack = false

    override val navigationChannel = Channel<NavigationIntent>(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    override fun goBack() {
        routesQueue.pollLast()
        navigationChannel.trySend(NavigationIntent.NavigateBack)
    }

    override fun navigate(route: CommonRoute) {
        (route as CommonHostRoute).let {
            if (!goBack && routesQueue.peekLast() != route) {
                routesQueue.offerLast(route)
            }
            goBack = false
            if (route == MainScreenRoutes.Main) {
                routesQueue.clear()
                routesQueue.offerLast(MainScreenRoutes.Main)
            }
            _routesFlow.update {
                route
            }
            Log.d("ROUTES", "navigate $routesQueue")
            navigationChannel.trySend(NavigationIntent.NavigateTo(route))
        }
    }

    override fun minimize() = Unit

    override fun goToPrevious() {
        goBack = true
        routesQueue.pollLast()
        val route =
            routesQueue.peekLast() ?: MainScreenRoutes.Main
        Log.d("ROUTES", "goToPrevious $routesQueue")
        navigate(route)
    }

    override fun getCurrentRoute() = routesQueue.peekLast() ?: MainScreenRoutes.Main
}

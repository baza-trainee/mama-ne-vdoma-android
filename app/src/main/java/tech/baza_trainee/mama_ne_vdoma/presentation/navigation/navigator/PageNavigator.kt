package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CommonHostRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CommonRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.MAIN_PAGE
import java.util.Deque
import java.util.LinkedList

interface PageNavigator: ScreenNavigator {

    val pagesFlow: StateFlow<CommonHostRoute>

    fun goToRoute(route: CommonHostRoute)

    fun goToPrevious()

    fun getCurrentRoute(): String
}

class PageNavigatorImpl: PageNavigator {

    override val pagesFlow: StateFlow<CommonHostRoute>
        get() = _pagesFlow.asStateFlow()
    private val _pagesFlow = MutableStateFlow<CommonHostRoute>(CommonHostRoute("", -1, ""))

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

    override fun goBackOnMain(scope: CoroutineScope) {
        routesQueue.pollLast()
        scope.launch {
            withContext(Dispatchers.Main) {
                navigationChannel.send(NavigationIntent.NavigateBack)
            }
        }
    }

    override fun navigate(route: CommonRoute) {
        if (!goBack) {
            routesQueue.offerLast(route as CommonHostRoute)
        }
        goBack = false
        navigationChannel.trySend(NavigationIntent.NavigateTo(route))
    }

    override fun navigateOnMain(scope: CoroutineScope, route: CommonRoute) {
        if (!goBack)
            routesQueue.offerLast(route as CommonHostRoute)
        goBack = false
        scope.launch {
            withContext(Dispatchers.Main) {
                navigationChannel.send(NavigationIntent.NavigateTo(route))
            }
        }
    }

    override fun goToRoute(route: CommonHostRoute) {
        if (route.page == MAIN_PAGE) {
            routesQueue.clear()
        }
        _pagesFlow.update {
            route
        }
    }

    override fun goToPrevious() {
        goBack = true
        routesQueue.pollLast()
        val page = routesQueue.peekLast() ?: MAIN_PAGE
        val route =
            routesQueue.peekLast() ?: MainScreenRoutes.Main
        if (page == MAIN_PAGE) {
            routesQueue.clear()
        }
        _pagesFlow.update {
            route
        }
    }

    override fun getCurrentRoute() = routesQueue.peekLast()?.route ?: MainScreenRoutes.Main.route
}
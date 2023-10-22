package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator

import android.util.Log
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

    val routesFlow: StateFlow<CommonHostRoute>

    fun goToPrevious()

    fun getCurrentRoute(): String
}

class PageNavigatorImpl: PageNavigator {

    override val routesFlow: StateFlow<CommonHostRoute>
        get() = _routesFlow.asStateFlow()
    private val _routesFlow = MutableStateFlow(CommonHostRoute("", -1, ""))

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
        (route as CommonHostRoute).let {
            if (!goBack) {
                routesQueue.offerLast(route)
            }
            goBack = false
            if (route.page == MAIN_PAGE) {
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

    override fun navigateOnMain(scope: CoroutineScope, route: CommonRoute) {
        (route as CommonHostRoute).let {
            if (!goBack) {
                routesQueue.offerLast(route)
            }
            goBack = false
            if (route.page == MAIN_PAGE) {
                routesQueue.clear()
                routesQueue.offerLast(MainScreenRoutes.Main)
            }
            _routesFlow.update {
                route
            }
            scope.launch {
                withContext(Dispatchers.Main) {
                    navigationChannel.send(NavigationIntent.NavigateTo(route))
                }
            }
        }
    }

    override fun goToPrevious() {
        goBack = true
        routesQueue.pollLast()
        val route =
            routesQueue.peekLast() ?: MainScreenRoutes.Main
        Log.d("ROUTES", "goToPrevious $routesQueue")
        navigate(route)
    }

    override fun getCurrentRoute() = routesQueue.peekLast()?.route ?: MainScreenRoutes.Main.route
}
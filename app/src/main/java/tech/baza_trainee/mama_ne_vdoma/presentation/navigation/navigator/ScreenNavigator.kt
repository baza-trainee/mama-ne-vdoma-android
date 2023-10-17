package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CommonRoute

interface ScreenNavigator {

    val navigationChannel: Channel<NavigationIntent>

    fun goBack()

    fun goBackOnMain(scope: CoroutineScope)

    fun navigate(route: CommonRoute)

    fun navigateOnMain(scope: CoroutineScope, route: CommonRoute)
}

class ScreenNavigatorImpl: ScreenNavigator {

    override val navigationChannel = Channel<NavigationIntent>(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    override fun goBack() {
        navigationChannel.trySend(NavigationIntent.NavigateBack)
    }

    override fun goBackOnMain(scope: CoroutineScope) {
        scope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                navigationChannel.send(NavigationIntent.NavigateBack)
            }
        }
    }

    override fun navigate(route: CommonRoute) {
        navigationChannel.trySend(NavigationIntent.NavigateTo(route))
    }

    override fun navigateOnMain(scope: CoroutineScope, route: CommonRoute) {
        scope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                navigationChannel.send(NavigationIntent.NavigateTo(route))
            }
        }
    }
}
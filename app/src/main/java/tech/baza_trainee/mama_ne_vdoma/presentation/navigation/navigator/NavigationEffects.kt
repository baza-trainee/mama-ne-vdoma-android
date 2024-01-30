package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>,
    navHostController: NavHostController
) {
    val activity = LocalContext.current.findActivity()
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity.isFinishing) {
                return@collect
            }
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (navHostController.currentBackStackEntry?.lifecycle?.currentState ==
                        Lifecycle.State.RESUMED)
                        navHostController.popBackStack()
                }

                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route.route)
                }

                NavigationIntent.Minimize -> activity.moveTaskToBack(true)
            }
        }
    }
}
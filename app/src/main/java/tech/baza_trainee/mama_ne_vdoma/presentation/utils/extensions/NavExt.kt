package tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.ScreenNavigator


@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel()
    val parentEntry = remember(this) { navController.getBackStackEntry(navGraphRoute) }
    return koinNavViewModel(viewModelStoreOwner = parentEntry)
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    screenNavigator: ScreenNavigator?
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel()
    val parentEntry = remember(this) { screenNavigator?.getBackStackEntry(navGraphRoute) }
    return koinNavViewModel(viewModelStoreOwner = parentEntry ?: this)
}

fun NavHostController.navigateWithArgs(route: String, argument: String? = null) {
    val newRoute = argument?.let { route.plus(it) } ?: route
    navigate(newRoute)
}
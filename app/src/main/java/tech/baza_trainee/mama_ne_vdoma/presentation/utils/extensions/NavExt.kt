package tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.core.parameter.ParametersDefinition


@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
    noinline parameters: ParametersDefinition? = null
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel(parameters = parameters)
    val parentEntry = remember(this) { navController.getBackStackEntry(navGraphRoute) }
    return koinNavViewModel(viewModelStoreOwner = parentEntry, parameters = parameters)
}
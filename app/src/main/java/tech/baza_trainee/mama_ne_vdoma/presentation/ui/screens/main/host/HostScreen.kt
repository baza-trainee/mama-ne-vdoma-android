package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.groupNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.mainNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.searchNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.settingsNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.NavigationEffects
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.MainNavigationBar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ToolbarWithAvatar

@Composable
fun HostScreen(
    modifier: Modifier = Modifier,
    navigator: ScreenNavigator,
    handleEvent: (HostEvent) -> Unit = {}
) {
    SurfaceWithNavigationBars {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = { ToolbarWithAvatar() },
            bottomBar = {
                MainNavigationBar {
                    handleEvent(HostEvent.SwitchTab(it))
                }
            }
        ) {
            BackHandler { handleEvent(HostEvent.OnBack) }

            val navController = rememberNavController()

            NavigationEffects(
                navigationChannel = navigator.navigationChannel,
                navHostController = navController
            )

            NavHost(
                modifier = modifier
                    .padding(it)
                    .consumeWindowInsets(it),
                navController = navController,
                startDestination = Graphs.Host.Main.route
            ) {
                mainNavGraph()
                groupNavGraph()
                searchNavGraph()
                settingsNavGraph()
            }
        }
    }
}
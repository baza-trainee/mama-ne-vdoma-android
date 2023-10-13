package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.groupNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.mainNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.searchNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.settingsNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.NavigationEffects
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.MainNavigationBar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.MainNavigationItem
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ToolbarWithAvatar

@Composable
fun HostScreen(
    modifier: Modifier = Modifier,
    navigator: ScreenNavigator,
    screenState: State<HostViewState> = mutableStateOf(HostViewState()),
    handleEvent: (HostEvent) -> Unit = {}
) {
    SurfaceWithNavigationBars {

        val tabContents = listOf(
            MainNavigationItem("Головна", R.drawable.ic_home),
            MainNavigationItem("Групи", R.drawable.ic_group),
            MainNavigationItem("Пошук", R.drawable.ic_search),
            MainNavigationItem("Налаштування", R.drawable.ic_settings)
        )

        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                ToolbarWithAvatar(
                    title = tabContents[screenState.value.currentPage].title
                )
            },
            bottomBar = {
                MainNavigationBar(
                    items = tabContents,
                    currentPage = screenState.value.currentPage
                ) {
                    handleEvent(HostEvent.SwitchTab(it))
                }
            }
        ) {
            val navController = rememberNavController()

            NavigationEffects(
                navigationChannel = navigator.navigationChannel,
                navHostController = navController
            )

            NavHost(
                modifier = modifier
                    .padding(it)
                    .consumeWindowInsets(it)
                    .padding(horizontal = 24.dp),
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
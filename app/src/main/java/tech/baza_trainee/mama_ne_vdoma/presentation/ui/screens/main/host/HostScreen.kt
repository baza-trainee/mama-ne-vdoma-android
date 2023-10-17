package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import android.widget.Toast
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.groupNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.mainNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.searchNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.settingsNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.NavigationEffects
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.MainNavigationBar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.MainNavigationItem
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.ToolbarWithAvatar
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

@Composable
fun HostScreen(
    modifier: Modifier = Modifier,
    navigator: PageNavigator,
    screenState: State<HostViewState> = mutableStateOf(HostViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (HostEvent) -> Unit = {}
) {
    SurfaceWithNavigationBars {
        val context = LocalContext.current

        when (val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(HostEvent.ResetUiState)
            }
        }

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
                    title = tabContents[screenState.value.currentPage].title,
                    avatar = screenState.value.avatar,
                    showArrow = screenState.value.currentPage != 0,
                    showNotification = screenState.value.notifications != 0,
                    notificationCount = screenState.value.notifications,
                    onBack = { handleEvent(HostEvent.OnBackLocal) }
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
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                navController = navController,
                startDestination = Graphs.HostNested.Main.route
            ) {
                mainNavGraph()
                groupNavGraph()
                searchNavGraph()
                settingsNavGraph()
            }
        }
    }
}
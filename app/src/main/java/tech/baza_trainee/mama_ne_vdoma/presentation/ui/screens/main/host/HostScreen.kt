package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.chatNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.groupNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.mainNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.searchNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.settingsNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.NavigationEffects
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigatorImpl
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.MainNavigationBar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.MainNavigationItem
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithToolbar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.ToolbarWithAvatar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SETTINGS_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.findActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun HostScreen(
    navigator: PageNavigator,
    screenState: HostViewState = HostViewState(),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (HostEvent) -> Unit = {}
) {
    SurfaceWithNavigationBars {
        val context = LocalContext.current

        val lifecycleOwner by remember { mutableStateOf(context.findActivity() as LifecycleOwner) }
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> handleEvent(HostEvent.OnCheckSession)

                    else -> Unit
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        when (val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                context.showToast(state.error)
                handleEvent(HostEvent.ResetUiState)
            }
        }

        val tabContents = listOf(
            MainNavigationItem(stringResource(id = R.string.title_main_page), R.drawable.ic_home),
            MainNavigationItem(stringResource(id = R.string.title_groups), R.drawable.ic_group),
            MainNavigationItem(stringResource(id = R.string.title_chat), R.drawable.ic_chat),
            MainNavigationItem(stringResource(id = R.string.title_search), R.drawable.ic_search),
            MainNavigationItem(stringResource(id = R.string.title_settings), R.drawable.ic_settings)
        )

        val notificationsCount = screenState.notifications

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (screenState.currentRoute == MainScreenRoutes.Main) {
                    ToolbarWithAvatar(
                        title = stringResource(id = screenState.currentRoute.title),
                        avatar = screenState.avatar,
                        showArrow = false,
                        showNotification = true,
                        notificationCount = notificationsCount,
                        onNotificationsClicked = { handleEvent(HostEvent.GoToNotifications) },
                        onAvatarClicked = { handleEvent(HostEvent.SwitchTab(SETTINGS_PAGE)) }
                    )
                } else {
                    HeaderWithToolbar(
                        title = stringResource(id = screenState.currentRoute.title),
                        avatar = screenState.avatar,
                        showNotification = notificationsCount != 0,
                        notificationCount = notificationsCount,
                        onNotificationsClicked = { handleEvent(HostEvent.GoToNotifications) },
                        onBack = { handleEvent(HostEvent.OnBackLocal) },
                        onAvatarClicked = { handleEvent(HostEvent.SwitchTab(SETTINGS_PAGE)) }
                    )
                }
            },
            bottomBar = {
                MainNavigationBar(
                    items = tabContents,
                    currentPage = screenState.currentRoute.page
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
                modifier = Modifier
                    .padding(it)
                    .consumeWindowInsets(it)
                    .fillMaxSize()
                    .padding(horizontal = size_16_dp),
                navController = navController,
                startDestination = Graphs.HostNested.Main.route
            ) {
                mainNavGraph()
                groupNavGraph()
                chatNavGraph()
                searchNavGraph()
                settingsNavGraph()
            }
        }
    }
}

@Composable
@Preview
fun HostScreenPreview() {
    HostScreen(
        navigator = PageNavigatorImpl(),
        screenState = HostViewState(),
        uiState = remember { mutableStateOf(RequestState.Idle) },
        handleEvent = {}
    )
}
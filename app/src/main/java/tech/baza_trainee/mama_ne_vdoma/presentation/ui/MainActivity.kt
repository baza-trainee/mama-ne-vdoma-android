package tech.baza_trainee.mama_ne_vdoma.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.android.inject
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.createUserNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.groupStandaloneScreensNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.loginNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host.hostNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.startNavGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.userProfileGraph
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.NavigationEffects
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.MAIN_PAGE
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme


class MainActivity : FragmentActivity() {

    private val viewModel: MainActivityViewModel by inject()

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Mama_ne_vdomaTheme {
                val navController = rememberNavController()

                val navigator: ScreenNavigator by inject()

                var checkAuth by rememberSaveable { mutableStateOf(true) }

                LaunchedEffect(key1 = checkAuth) {
                    if (checkAuth) {
                        viewModel.checkAuthorization()

                        viewModel.canAuthenticate.collect {
                            if (it && checkAndAuthenticate())
                                buildBiometricPrompt(
                                    onSuccess = {
                                        checkAuth = false
                                        navigator.navigate(
                                            HostScreenRoutes.Host.getDestination(MAIN_PAGE)
                                        )
                                    }
                                ).authenticate(buildBiometricPromptInfo())
                        }
                    }
                }

                NavigationEffects(
                    navigationChannel = navigator.navigationChannel,
                    navHostController = navController
                )

                NavHost(
                    navController = navController,
                    startDestination = Graphs.Start.route
                ) {
                    startNavGraph(navController)
                    loginNavGraph(navController)
                    createUserNavGraph()
                    userProfileGraph()
                    groupStandaloneScreensNavGraph()
                    hostNavGraph()
                }
            }
        }
    }

    private fun checkAndAuthenticate() =
        when (BiometricManager.from(this).canAuthenticate(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }

    private fun buildBiometricPromptInfo() = PromptInfo.Builder()
        .setTitle("Біометрична автентифікація")
        .setSubtitle("Використайте Ваш спосіб розблокування пристрою для входу в застосунок")
        .setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
        .build()

    private fun buildBiometricPrompt(onSuccess: () -> Unit): BiometricPrompt = BiometricPrompt(
        this,
        ContextCompat.getMainExecutor(this),
        object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(
                    applicationContext,
                    "Невдала спроба автентифікації",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    applicationContext,
                    "Невдала спроба автентифікації",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    companion object {

        private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
        private var showOneTapUI = true
    }
}
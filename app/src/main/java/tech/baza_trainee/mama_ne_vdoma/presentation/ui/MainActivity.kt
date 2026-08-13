package tech.baza_trainee.mama_ne_vdoma.presentation.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import org.koin.android.ext.android.inject
import tech.baza_trainee.mama_ne_vdoma.R
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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.MAIN_PAGE


class MainActivity : FragmentActivity() {

    private val viewModel: MainActivityViewModel by inject()

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    private lateinit var appUpdateLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        appUpdateLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) {
            Log.d("AUTO-UPDATE", "activityResult : ${it.resultCode}")
            if (it.resultCode != RESULT_OK)
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }

        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                Log.d("AUTO-UPDATE", "${appUpdateInfo.availableVersionCode()}")
                checkAppUpdates(appUpdateInfo)
            }
            .addOnFailureListener {
                Log.d("AUTO-UPDATE", it.toString())
            }

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
                                            HostScreenRoutes.Host(MAIN_PAGE)
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
                    modifier = Modifier.windowInsetsPadding(WindowInsets.ime),
                    navController = navController,
                    startDestination = Graphs.Start
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
        .setTitle(getString(R.string.bio_auth_title))
        .setSubtitle(getString(R.string.bio_auth_subtitle))
        .setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
        .build()

    private fun buildBiometricPrompt(onSuccess: () -> Unit): BiometricPrompt = BiometricPrompt(
        this,
        ContextCompat.getMainExecutor(this),
        object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                    errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON
                ) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.bio_attempt_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.bio_attempt_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    private fun checkAppUpdates(appUpdateInfo: AppUpdateInfo) {
        if (isUpdateAvailable(appUpdateInfo)) {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                appUpdateLauncher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            )
        }
    }
    private fun isUpdateAvailable(appUpdateInfo: AppUpdateInfo): Boolean =
        appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ||
                appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)

}
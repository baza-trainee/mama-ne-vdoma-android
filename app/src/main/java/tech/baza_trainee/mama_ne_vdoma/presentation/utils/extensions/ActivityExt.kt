package tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun Activity.beginSignInGoogleOneTap(
    oneTapClient: SignInClient?,
    signInRequest: BeginSignInRequest
): BeginSignInResult =
    suspendCancellableCoroutine { continuation ->
        oneTapClient?.beginSignIn(signInRequest)
            ?.addOnSuccessListener(this) { result ->
                continuation.resume(result) { it.printStackTrace() }
            }
            ?.addOnFailureListener(this) { e ->
                continuation.resumeWithException(e)
            }
            ?.addOnCanceledListener {
                continuation.cancel()
            }
    }
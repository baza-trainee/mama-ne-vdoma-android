package tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions

import android.app.Activity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import tech.baza_trainee.mama_ne_vdoma.BuildConfig

sealed interface AuthCredential {
    data class GoogleIdToken(val token: String) : AuthCredential
    data class Password(val username: String, val password: String) : AuthCredential
}

/**
 * Requests a credential via Credential Manager.
 *
 * When [filterByAuthorizedAccounts] is true and the user has no account previously authorized for
 * this app, the request fails with [NoCredentialException]; in that case it is retried with every
 * Google account on the device.
 *
 * Returns null when no usable credential was obtained; throws on unexpected failures.
 */
suspend fun Activity.requestAuthCredential(
    credentialManager: CredentialManager?,
    includePassword: Boolean = false,
    autoSelectEnabled: Boolean = false,
    filterByAuthorizedAccounts: Boolean = false
): AuthCredential? {
    credentialManager ?: return null

    val credential = try {
        credentialManager.getCredential(
            this,
            buildRequest(includePassword, autoSelectEnabled, filterByAuthorizedAccounts)
        ).credential
    } catch (exc: NoCredentialException) {
        if (!filterByAuthorizedAccounts) throw exc
        credentialManager.getCredential(
            this,
            buildRequest(includePassword, autoSelectEnabled, filterByAuthorizedAccounts = false)
        ).credential
    }

    return credential.toAuthCredential()
}

private fun buildRequest(
    includePassword: Boolean,
    autoSelectEnabled: Boolean,
    filterByAuthorizedAccounts: Boolean
) = GetCredentialRequest.Builder()
    .apply { if (includePassword) addCredentialOption(GetPasswordOption()) }
    .addCredentialOption(
        GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.ONE_TAP_SERVER_CLIENT_ID)
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            .setAutoSelectEnabled(autoSelectEnabled)
            .build()
    )
    .build()

private fun Credential.toAuthCredential(): AuthCredential? = when {
    this is PasswordCredential -> AuthCredential.Password(id, password)

    this is CustomCredential &&
            type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL ->
        AuthCredential.GoogleIdToken(GoogleIdTokenCredential.createFrom(data).idToken)

    else -> null
}

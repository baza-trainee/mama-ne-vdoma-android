package tech.baza_trainee.mama_ne_vdoma.domain.preferences

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_ACCOUNT_AUTH_TOKEN
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_ACCOUNT_COOKIES
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_ACCOUNT_FCM_TOKEN
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_ACCOUNT_LOGIN
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_ADDRESS
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_ADMIN_JOIN_REQUESTS
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_AVATAR
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_AVATAR_URI
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_CHILDREN_PROVIDED
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_COUNTRY_CODE
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_CURRENT_CHILD
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_EMAIL
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_ID
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_LOCATION_LAT
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_LOCATION_LNG
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_MESSAGES
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_MY_JOIN_REQUESTS
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_NAME
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_NOTE
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_PHONE_NUMBER
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_PROFILE_FILLED
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_RADIUS
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_SEND_EMAIL
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.get
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.set
import java.io.IOException

class UserPreferencesDatastoreManager(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES)

    private val userDataStore: DataStore<Preferences> get() = context.dataStore

    val userPreferencesFlow: Flow<UserPreferences> = userDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val id = preferences[KEY_ID].orEmpty()
            val avatar = preferences[KEY_AVATAR].orEmpty()
            val avatarUri = Uri.parse(preferences[KEY_AVATAR_URI].orEmpty())
            val name = preferences[KEY_NAME].orEmpty()
            val code = preferences[KEY_COUNTRY_CODE].orEmpty()
            val phone = preferences[KEY_PHONE_NUMBER].orEmpty()
            val note = preferences[KEY_NOTE].orEmpty()
            val email = preferences[KEY_EMAIL].orEmpty()
            val address = preferences[KEY_ADDRESS].orEmpty()
            val radius = preferences[KEY_RADIUS] ?: 0
            val latitude = preferences[KEY_LOCATION_LAT] ?: 0.00
            val longitude = preferences[KEY_LOCATION_LNG] ?: 0.00
            val sendEmail = preferences[KEY_SEND_EMAIL] ?: true
            val profileFilled = preferences[KEY_PROFILE_FILLED] ?: true
            val childrenProvided = preferences[KEY_CHILDREN_PROVIDED] ?: true
            val currentChild = preferences[KEY_CURRENT_CHILD].orEmpty()
            val myJoinRequests = preferences[KEY_MY_JOIN_REQUESTS] ?: 0
            val adminJoinRequests = preferences[KEY_ADMIN_JOIN_REQUESTS] ?: 0
            val login = preferences[KEY_ACCOUNT_LOGIN].orEmpty()
            val authToken = preferences[KEY_ACCOUNT_AUTH_TOKEN].orEmpty()
            val fcmToken = preferences[KEY_ACCOUNT_FCM_TOKEN].orEmpty()
            val cookies = preferences[KEY_ACCOUNT_COOKIES].orEmpty()
            val messages = preferences[KEY_MESSAGES].orEmpty()
            UserPreferences(
                id, avatar, avatarUri, name, code,
                phone, email, address, radius,
                latitude, longitude, sendEmail,
                profileFilled, childrenProvided, currentChild,
                myJoinRequests, adminJoinRequests, login, authToken,
                fcmToken, cookies, note, messages
            )
        }

    var id: String
        get() = userDataStore.get(KEY_ID, "")
        set(value) = userDataStore.set(KEY_ID, value)

    var avatar: String
        get() = userDataStore.get(KEY_AVATAR, "")
        set(value) = userDataStore.set(KEY_AVATAR, value)


    var avatarUri: Uri
        get() = Uri.parse(userDataStore.get(KEY_AVATAR_URI, ""))
        set(value) = userDataStore.set(KEY_AVATAR_URI, value.toString())

    var name: String
        get() = userDataStore.get(KEY_NAME, "")
        set(value) = userDataStore.set(KEY_NAME, value)

    var code: String
        get() = userDataStore.get(KEY_COUNTRY_CODE, "")
        set(value) = userDataStore.set(KEY_COUNTRY_CODE, value)

    var phone: String
        get() = userDataStore.get(KEY_PHONE_NUMBER, "")
        set(value) = userDataStore.set(KEY_PHONE_NUMBER, value)

    var email: String
        get() = userDataStore.get(KEY_EMAIL, "")
        set(value) = userDataStore.set(KEY_EMAIL, value)

    var address: String
        get() = userDataStore.get(KEY_ADDRESS, "")
        set(value) = userDataStore.set(KEY_ADDRESS, value)

    var radius: Int
        get() = userDataStore.get(KEY_RADIUS, 0)
        set(value) = userDataStore.set(KEY_RADIUS, value)

    var latitude: Double
        get() = userDataStore.get(KEY_LOCATION_LAT, 0.00)
        set(value) = userDataStore.set(KEY_LOCATION_LAT, value)

    var longitude: Double
        get() = userDataStore.get(KEY_LOCATION_LNG, 0.00)
        set(value) = userDataStore.set(KEY_LOCATION_LNG, value)

    var sendEmail: Boolean
        get() = userDataStore.get(KEY_SEND_EMAIL, true)
        set(value) = userDataStore.set(KEY_SEND_EMAIL, value)

    var isChildrenDataProvided: Boolean
        get() = userDataStore.get(KEY_CHILDREN_PROVIDED, true)
        set(value) = userDataStore.set(KEY_CHILDREN_PROVIDED, value)

    var isUserProfileFilled: Boolean
        get() = userDataStore.get(KEY_PROFILE_FILLED, true)
        set(value) = userDataStore.set(KEY_PROFILE_FILLED, value)

    var currentChild: String
        get() = userDataStore.get(KEY_CURRENT_CHILD, "")
        set(value) = userDataStore.set(KEY_CURRENT_CHILD, value)

    var myJoinRequests: Int
        get() = userDataStore.get(KEY_MY_JOIN_REQUESTS, 0)
        set(value) = userDataStore.set(KEY_MY_JOIN_REQUESTS, value)

    var adminJoinRequests: Int
        get() = userDataStore.get(KEY_ADMIN_JOIN_REQUESTS, 0)
        set(value) = userDataStore.set(KEY_ADMIN_JOIN_REQUESTS, value)

    var login: String
        get() = userDataStore.get(KEY_ACCOUNT_LOGIN, "")
        set(value) = userDataStore.set(KEY_ACCOUNT_LOGIN, value)

    var authToken: String
        get() = userDataStore.get(KEY_ACCOUNT_LOGIN, "")
        set(value) = userDataStore.set(KEY_ACCOUNT_LOGIN, value)

    var fcmToken: String
        get() = userDataStore.get(KEY_ACCOUNT_FCM_TOKEN, "")
        set(value) = userDataStore.set(KEY_ACCOUNT_FCM_TOKEN, value)

    var cookies: Set<String>
        get() = userDataStore.get(KEY_ACCOUNT_COOKIES, emptySet())
        set(value) = userDataStore.set(KEY_ACCOUNT_COOKIES, value)

    var note: String
        get() = userDataStore.get(KEY_NOTE, "")
        set(value) = userDataStore.set(KEY_NOTE, value)

    var messages: Set<String>
        get() = userDataStore.get(KEY_MESSAGES, emptySet())
        set(value) = userDataStore.set(KEY_MESSAGES, value)

    fun clearData() = runBlocking {
        withContext(Dispatchers.Default) {
            userDataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }
}

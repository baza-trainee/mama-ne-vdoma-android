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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_ACCOUNT_AUTH_TOKEN
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
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_MY_JOIN_REQUESTS
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_NAME
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_PHONE_NUMBER
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_PROFILE_FILLED
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_RADIUS
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_SEND_EMAIL
import java.io.IOException

class UserPreferencesDatastoreManager(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES
    )

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
            UserPreferences(
                id, avatar, avatarUri, name, code,
                phone, email, address, radius,
                latitude, longitude, sendEmail,
                profileFilled, childrenProvided, currentChild,
                myJoinRequests, adminJoinRequests, login, authToken
            )
        }

    var id: String
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().id
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_ID] = value
                }
            }
        }

    var avatar: String
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().avatar
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_AVATAR] = value
                }
            }
        }

    var avatarUri: Uri
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().avatarUri
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_AVATAR_URI] = value.toString()
                }
            }
        }

    var name: String
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().name
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_NAME] = value
                }
            }
        }

    var code: String
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().code
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_COUNTRY_CODE] = value
                }
            }
        }

    var phone: String
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().phone
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_PHONE_NUMBER] = value
                }
            }
        }

    var email: String
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().email
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_EMAIL] = value
                }
            }
        }

    var address: String
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().address
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_ADDRESS] = value
                }
            }
        }

    var radius: Int
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().radius
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_RADIUS] = value
                }
            }
        }

    var latitude: Double
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().latitude
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_LOCATION_LAT] = value
                }
            }
        }

    var longitude: Double
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().longitude
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_LOCATION_LNG] = value
                }
            }
        }

    var sendEmail: Boolean
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().sendEmail
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_SEND_EMAIL] = value
                }
            }
        }

    var isChildrenDataProvided: Boolean
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().isChildrenDataProvided
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_CHILDREN_PROVIDED] = value
                }
            }
        }

    var isUserProfileFilled: Boolean
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().isUserProfileFilled
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_PROFILE_FILLED] = value
                }
            }
        }

    var currentChild: String
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().currentChild
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_CURRENT_CHILD] = value
                }
            }
        }

    var myJoinRequests: Int
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().myJoinRequests
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_MY_JOIN_REQUESTS] = value
                }
            }
        }

    var adminJoinRequests: Int
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().adminJoinRequests
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_ADMIN_JOIN_REQUESTS] = value
                }
            }
        }

    var login: String
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().login
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_ACCOUNT_LOGIN] = value
                }
            }
        }

    var authToken: String
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().authToken
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_ACCOUNT_AUTH_TOKEN] = value
                }
            }
        }

    fun clearData() = runBlocking {
        withContext(Dispatchers.Default) {
            userDataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }
}
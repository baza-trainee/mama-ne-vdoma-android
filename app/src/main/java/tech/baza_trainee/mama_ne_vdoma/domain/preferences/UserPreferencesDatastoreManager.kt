package tech.baza_trainee.mama_ne_vdoma.domain.preferences

import android.content.Context
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
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_ADDRESS
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_AVATAR
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_COUNTRY_CODE
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_EMAIL
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_ID
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_LOCATION_LAT
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_LOCATION_LNG
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_NAME
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_NOTIFICATION
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_PHONE_NUMBER
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_RADIUS
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesKeys.KEY_SEND_EMAIL
import java.io.IOException

class UserPreferencesDatastoreManager(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES
    )

    private val userDataStore: DataStore<Preferences> get() = context.dataStore

    private val userPreferencesFlow: Flow<UserPreferences> = userDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val id = preferences[KEY_ID].orEmpty()
            val avatar = preferences[KEY_AVATAR].orEmpty()
            val name = preferences[KEY_NAME].orEmpty()
            val code = preferences[KEY_COUNTRY_CODE].orEmpty()
            val phone = preferences[KEY_PHONE_NUMBER].orEmpty()
            val email = preferences[KEY_EMAIL].orEmpty()
            val address = preferences[KEY_ADDRESS].orEmpty()
            val radius = preferences[KEY_RADIUS] ?: 0
            val latitude = preferences[KEY_LOCATION_LAT] ?: 0.00
            val longitude = preferences[KEY_LOCATION_LNG] ?: 0.00
            val notificationCount = preferences[KEY_NOTIFICATION] ?: 0
            val sendEmail = preferences[KEY_SEND_EMAIL] ?: true
            UserPreferences(id, avatar, name, code, phone, email, address, radius, latitude, longitude, notificationCount, sendEmail)
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

    var notifications: Int
        get() = runBlocking {
            withContext(Dispatchers.Default) {
                userPreferencesFlow.first().notificationCount
            }
        }
        set(value) = runBlocking {
            withContext(Dispatchers.Default ) {
                userDataStore.edit {
                    it[KEY_NOTIFICATION] = value
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
}
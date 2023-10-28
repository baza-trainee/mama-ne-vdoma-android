package tech.baza_trainee.mama_ne_vdoma.domain.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    val KEY_ID = stringPreferencesKey(ID)
    val KEY_AVATAR = stringPreferencesKey(AVATAR)
    val KEY_AVATAR_URI = stringPreferencesKey(AVATAR_URI)
    val KEY_NAME = stringPreferencesKey(NAME)
    val KEY_COUNTRY_CODE = stringPreferencesKey(COUNTRY_CODE)
    val KEY_PHONE_NUMBER = stringPreferencesKey(PHONE_NUMBER)
    val KEY_ADDRESS = stringPreferencesKey(ADDRESS)
    val KEY_EMAIL = stringPreferencesKey(EMAIL)
    val KEY_RADIUS = intPreferencesKey(RADIUS)
    val KEY_LOCATION_LAT = doublePreferencesKey(LOCATION_LAT)
    val KEY_LOCATION_LNG = doublePreferencesKey(LOCATION_LNG)
    val KEY_SEND_EMAIL = booleanPreferencesKey(SEND_EMAIL)
    val KEY_PROFILE_FILLED = booleanPreferencesKey(PROFILE_FILLED)
    val KEY_CHILDREN_PROVIDED = booleanPreferencesKey(CHILDREN_PROVIDED)
    val KEY_CURRENT_CHILD = stringPreferencesKey(CURRENT_CHILD)
    val KEY_MY_JOIN_REQUESTS = intPreferencesKey(MY_JOIN_REQUESTS)
    val KEY_ADMIN_JOIN_REQUESTS = intPreferencesKey(ADMIN_JOIN_REQUESTS)
    val KEY_ACCOUNT_LOGIN = stringPreferencesKey(ACCOUNT_LOGIN)
    val KEY_ACCOUNT_AUTH_TOKEN = stringPreferencesKey(ACCOUNT_AUTH_TOKEN)
}
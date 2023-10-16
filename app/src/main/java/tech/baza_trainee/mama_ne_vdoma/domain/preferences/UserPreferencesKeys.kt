package tech.baza_trainee.mama_ne_vdoma.domain.preferences

import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    val KEY_ID = stringPreferencesKey(ID)
    val KEY_AVATAR = stringPreferencesKey(AVATAR)
    val KEY_NAME = stringPreferencesKey(NAME)
    val KEY_COUNTRY_CODE = stringPreferencesKey(COUNTRY_CODE)
    val KEY_PHONE_NUMBER = stringPreferencesKey(PHONE_NUMBER)
    val KEY_ADDRESS = stringPreferencesKey(ADDRESS)
    val KEY_EMAIL = stringPreferencesKey(EMAIL)
    val KEY_RADIUS = intPreferencesKey(RADIUS)
    val KEY_LOCATION_LAT = doublePreferencesKey(LOCATION_LAT)
    val KEY_LOCATION_LNG = doublePreferencesKey(LOCATION_LNG)
}
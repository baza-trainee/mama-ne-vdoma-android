package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.ChildNameViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.Period
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.ScheduleScreenState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserLocationViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.decodeBitmap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek

class UserSettingsViewModel(
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _locationScreenState = MutableStateFlow(UserLocationViewState())
    val locationScreenState: StateFlow<UserLocationViewState> = _locationScreenState.asStateFlow()

    private val _userInfoScreenState = MutableStateFlow(UserInfoViewState())
    val userInfoScreenState: StateFlow<UserInfoViewState> = _userInfoScreenState.asStateFlow()

    private val _childNameScreenState = MutableStateFlow(ChildNameViewState())
    val childNameScreenState: StateFlow<ChildNameViewState> = _childNameScreenState.asStateFlow()

    private val _childScheduleScreenState = MutableStateFlow(ScheduleScreenState())
    val childScheduleScreenState: StateFlow<ScheduleScreenState> = _childScheduleScreenState.asStateFlow()

    private val _parentScheduleScreenState = MutableStateFlow(ScheduleScreenState())
    val parentScheduleScreenState: StateFlow<ScheduleScreenState> = _parentScheduleScreenState.asStateFlow()

    private var uriForCrop: Uri = Uri.EMPTY

    fun setUriForCrop(uri: Uri) {
        uriForCrop = uri
    }

    fun getBitmapForCrop(contentResolver: ContentResolver) = uriForCrop.decodeBitmap(contentResolver)

    fun saveUserAvatar(image: Bitmap) {
        _userInfoScreenState.update {
            it.copy(
                userAvatar = image
            )
        }
//        val deferred = coroutineScope.async(Dispatchers.IO) {
//            val filename = "cropped_avatar"
//            context.openFileOutput(filename, Context.MODE_PRIVATE)
//                .use {
//                    croppedImage?.asAndroidBitmap()
//                        ?.compress(Bitmap.CompressFormat.JPEG, 100, it)
//                    it.flush()
//                    it.close()
//                }
//            return@async File(context.filesDir, filename)
//        }
    }

    fun setCode(code: String) {
        _userInfoScreenState.update {
            it.copy(
                code = code
            )
        }
    }

    fun validatePhone(phone: String) {
        val phoneValid = if (phone.length in PHONE_LENGTH && phone.none { !it.isDigit() }) ValidField.VALID
        else ValidField.INVALID
        _userInfoScreenState.update {
            it.copy(
                userPhone = phone,
                phoneValid = phoneValid
            )
        }
    }

    fun validateUserName(name: String) {
        val nameValid = if (name.none { !it.isLetter() }) ValidField.VALID
        else ValidField.INVALID
        _userInfoScreenState.update {
            it.copy(
                userName = name,
                nameValid = nameValid
            )
        }
    }

    fun validateChildName(name: String) {
        val nameValid = if (name.none { !it.isLetter() }) ValidField.VALID
        else ValidField.INVALID
        _childNameScreenState.update {
            it.copy(
                name = name,
                nameValid = nameValid
            )
        }
    }

    fun validateAge(age: String) {
        val intAge = age.toFloatOrNull()

        val ageValid = if (intAge != null && intAge in 0f..MAX_AGE) ValidField.VALID
        else ValidField.INVALID

        _childNameScreenState.update {
            it.copy(
                age = age,
                ageValid = ageValid
            )
        }
    }

    fun setGender(gender: Gender) {
        _childNameScreenState.update {
            it.copy( gender = gender)
        }
    }

    fun updateChildSchedule(dayOfWeek: DayOfWeek, dayPeriod: Period) {
        val currentSchedule = _childScheduleScreenState.value.schedule
        when (dayPeriod) {
            Period.WHOLE_DAY -> {
                _childScheduleScreenState.update {
                    it.copy(
                        schedule = currentSchedule.apply {
                            schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                wholeDay = schedule[dayOfWeek]?.wholeDay?.not() ?: false
                            ) ?: DayPeriod()
                            if (schedule[dayOfWeek]?.wholeDay == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    morning = false,
                                    noon = false,
                                    afternoon = false
                                ) ?: DayPeriod()
                            }
                        }
                    )
                }
            }

            Period.MORNING -> {
                _childScheduleScreenState.update {
                    it.copy(
                        schedule = currentSchedule.apply {
                            schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                morning = schedule[dayOfWeek]?.morning?.not() ?: false
                            ) ?: DayPeriod()
                            if (schedule[dayOfWeek]?.morning == true &&
                                schedule[dayOfWeek]?.noon == true &&
                                schedule[dayOfWeek]?.afternoon == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = true,
                                    morning = false,
                                    noon = false,
                                    afternoon = false
                                ) ?: DayPeriod()
                            } else if (schedule[dayOfWeek]?.morning == true && schedule[dayOfWeek]?.wholeDay == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = false
                                ) ?: DayPeriod()
                            }
                        }
                    )
                }
            }

            Period.NOON -> {
                _childScheduleScreenState.update {
                    it.copy(
                        schedule = currentSchedule.apply {
                            schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                noon = schedule[dayOfWeek]?.noon?.not() ?: false
                            ) ?: DayPeriod()
                            if (schedule[dayOfWeek]?.morning == true &&
                                schedule[dayOfWeek]?.noon == true &&
                                schedule[dayOfWeek]?.afternoon == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = true,
                                    morning = false,
                                    noon = false,
                                    afternoon = false
                                ) ?: DayPeriod()
                            } else if (schedule[dayOfWeek]?.noon == true && schedule[dayOfWeek]?.wholeDay == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = false
                                ) ?: DayPeriod()
                            }
                        }
                    )
                }
            }

            Period.AFTERNOON -> {
                _childScheduleScreenState.update {
                    it.copy(
                        schedule = currentSchedule.apply {
                            schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                afternoon = schedule[dayOfWeek]?.afternoon?.not() ?: false
                            ) ?: DayPeriod()
                            if (schedule[dayOfWeek]?.morning == true &&
                                schedule[dayOfWeek]?.noon == true &&
                                schedule[dayOfWeek]?.afternoon == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = true,
                                    morning = false,
                                    noon = false,
                                    afternoon = false
                                ) ?: DayPeriod()
                            } else if (schedule[dayOfWeek]?.afternoon == true && schedule[dayOfWeek]?.wholeDay == true) {
                                schedule[dayOfWeek] = schedule[dayOfWeek]?.copy(
                                    wholeDay = false
                                ) ?: DayPeriod()
                            }
                        }
                    )
                }
            }
        }
    }

    fun requestCurrentLocation() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getCurrentLocation()
            }
            onSuccess { location ->
                location?.let {
                    _locationScreenState.update {
                        it.copy(
                            currentLocation = location
                        )
                    }
                }
            }
            onError {  }
            onLoading {  }
        }
    }

    fun getLocationFromAddress(address: String) {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getLocationFromAddress(address)
            }
            onSuccess { location ->
                location?.let {
                    _locationScreenState.update {
                        it.copy(
                            currentLocation = location
                        )
                    }
                }
            }
            onError {  }
            onLoading {  }
        }
    }

    fun getAddressFromLocation(latLng: LatLng) {
        networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess {  }
            onError {  }
            onLoading {  }
        }
    }

    companion object {

        private val PHONE_LENGTH = 9..12
        private const val MAX_AGE = 18f
    }
}
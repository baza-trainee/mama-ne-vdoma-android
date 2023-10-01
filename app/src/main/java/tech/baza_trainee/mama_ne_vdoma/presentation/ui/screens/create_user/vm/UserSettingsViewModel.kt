package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.Child
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.ChildInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.ChildrenInfoScreenState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.ScheduleScreenState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserLocationViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.decodeBitmap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek
import java.util.UUID

class UserSettingsViewModel(
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _locationScreenState = MutableStateFlow(UserLocationViewState())
    val locationScreenState: StateFlow<UserLocationViewState> = _locationScreenState.asStateFlow()

    private val _userInfoScreenState = MutableStateFlow(UserInfoViewState())
    val userInfoScreenState: StateFlow<UserInfoViewState> = _userInfoScreenState.asStateFlow()

    private val _childInfoScreenState = MutableStateFlow(ChildInfoViewState())
    val childInfoScreenState: StateFlow<ChildInfoViewState> = _childInfoScreenState.asStateFlow()

    private val _childScheduleScreenState = MutableStateFlow(ScheduleScreenState())
    val childScheduleScreenState: StateFlow<ScheduleScreenState> = _childScheduleScreenState.asStateFlow()

    private val _parentScheduleScreenState = MutableStateFlow(ScheduleScreenState())
    val parentScheduleScreenState: StateFlow<ScheduleScreenState> = _parentScheduleScreenState.asStateFlow()

    private val _childrenInfoScreenState = MutableStateFlow(ChildrenInfoScreenState())
    val childrenInfoScreenState: StateFlow<ChildrenInfoScreenState> = _childrenInfoScreenState.asStateFlow()

    var userAddress by mutableStateOf("")
        private set

    var userName by mutableStateOf("")
        private set

    var userPhone by mutableStateOf("")
        private set

    var userAvatar by mutableStateOf<Bitmap?>(null)
        private set

    var countryCode by mutableStateOf("")
        private set

    var childName by mutableStateOf("")
        private set

    var childAge by mutableStateOf("")
        private set

    var childComment = mutableStateOf("")
        private set

    private var uriForCrop: Uri = Uri.EMPTY

    private var currentChildId = ""

    fun setUriForCrop(uri: Uri) {
        uriForCrop = uri
    }

    fun getBitmapForCrop(contentResolver: ContentResolver) = uriForCrop.decodeBitmap(contentResolver)

    fun saveUserAvatar(image: Bitmap) {
        userAvatar = image
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
        countryCode = code
    }

    fun validatePhone(phone: String) {
        userPhone = phone
        val phoneValid = if (phone.length in PHONE_LENGTH && phone.none { !it.isDigit() }) ValidField.VALID
        else ValidField.INVALID
        _userInfoScreenState.update {
            it.copy(
                phoneValid = phoneValid
            )
        }
    }

    fun validateUserName(name: String) {
        userName = name
        val nameValid = if (name.length in NAME_LENGTH && name.all { it.isLetter() || it.isDigit() }) ValidField.VALID
        else ValidField.INVALID
        _userInfoScreenState.update {
            it.copy(
                nameValid = nameValid
            )
        }
    }

    fun validateChildName(name: String) {
        childName = name
        val nameValid = if (name.none { !it.isLetter() }) ValidField.VALID
        else ValidField.INVALID
        _childInfoScreenState.update {
            it.copy(
                nameValid = nameValid
            )
        }
    }

    fun validateAge(age: String) {
        childAge =  age

        val intAge = age.toFloatOrNull()

        val ageValid = if (intAge != null && intAge in 0f..MAX_AGE) ValidField.VALID
        else ValidField.INVALID

        _childInfoScreenState.update {
            it.copy(
                ageValid = ageValid
            )
        }
    }

    fun setGender(gender: Gender) {
        _childInfoScreenState.update {
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

        with(_childrenInfoScreenState.value.children.toMutableList()) {
            val child = firstOrNull { it.id == currentChildId }
            if (child != null) {
                val index = indexOf(child)
                val newChild = child.copy(
                    schedule = _childScheduleScreenState.value.schedule,
                )
                this[index] = newChild
            } else
                add(
                    Child(
                        id = currentChildId,
                        name = childName,
                        age = childAge,
                        gender = _childInfoScreenState.value.gender,
                        schedule = _childScheduleScreenState.value.schedule,
                        comment = childComment.value
                    )
                )
            _childrenInfoScreenState.update {
                it.copy(
                    children = this
                )
            }
        }
    }

    fun updateChildComment(comment: String) {
        this.childComment.value = comment
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

    fun updateUserAddress(address: String) {
        this.userAddress = address
    }

    fun getLocationFromAddress() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getLocationFromAddress(userAddress)
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

    fun resetCurrentChild() {
        currentChildId = ""
    }

    fun deleteChild(childId: String) {
        with(_childrenInfoScreenState.value.children.toMutableList()) {
            val child = firstOrNull { it.id == childId }
            remove(child)
            _childrenInfoScreenState.update {
                it.copy(
                    children = this
                )
            }
        }
    }

    fun setCurrentChild(childId: String = "") {
        currentChildId = when {
            childId.isNotEmpty() -> childId
            currentChildId.isEmpty() -> UUID.randomUUID().toString()
            else -> currentChildId
        }
        val currentChild =
            _childrenInfoScreenState.value.children.firstOrNull() { it.id == currentChildId }
                ?: Child(
                    id = currentChildId
                )
        childName = currentChild.name
        childAge= currentChild.age
        _childInfoScreenState.update {
            it.copy(
                nameValid = ValidField.VALID,
                ageValid = ValidField.VALID,
                gender = currentChild.gender
            )
        }
    }

    fun saveCurrentChild() {
        val list = _childrenInfoScreenState.value.children.toMutableList()
        val child = list.firstOrNull { it.id == currentChildId }
        if (child != null) {
            val index = list.indexOf(child)
            val newChild = child.copy(
                name = childName,
                age = childAge,
                gender = _childInfoScreenState.value.gender
            )
            list[index] = newChild
        } else
            list.add(
                Child(
                    id = currentChildId,
                    name = childName,
                    age = childAge,
                    gender = _childInfoScreenState.value.gender
                )
            )
        _childrenInfoScreenState.update {
            it.copy(
                children = list
            )
        }
    }

    fun setCurrentChildSchedule() {
        with(_childrenInfoScreenState.value.children.toMutableList()) {
            val child = firstOrNull { it.id == currentChildId }
            childComment.value = child?.comment.orEmpty()
            _childScheduleScreenState.update {
                it.copy(
                    schedule = child?.schedule ?: ScheduleModel()
                )
            }
        }
    }

    companion object {

        private val PHONE_LENGTH = 9..12
        private val NAME_LENGTH = 6..18
        private const val MAX_AGE = 18f
    }
}
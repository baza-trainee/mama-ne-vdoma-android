package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm

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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildrenInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildrenInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserLocationEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserLocationViewState
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

    private val _childScheduleViewState = MutableStateFlow(ScheduleViewState())
    val childScheduleViewState: StateFlow<ScheduleViewState> = _childScheduleViewState.asStateFlow()

    private val _parentScheduleViewState = MutableStateFlow(ScheduleViewState())
    val parentScheduleViewState: StateFlow<ScheduleViewState> = _parentScheduleViewState.asStateFlow()

    private val _childrenInfoViewState = MutableStateFlow(ChildrenInfoViewState())
    val childrenInfoViewState: StateFlow<ChildrenInfoViewState> = _childrenInfoViewState.asStateFlow()

    var userAddress by mutableStateOf("")
        private set

    var userName by mutableStateOf("")
        private set

    var userPhone by mutableStateOf("")
        private set

    var childName by mutableStateOf("")
        private set

    var childAge by mutableStateOf("")
        private set

    var childComment = mutableStateOf("")
        private set

    private var uriForCrop: Uri = Uri.EMPTY

    private var currentChildId = ""

    fun handleUserInfoEvent(event: UserInfoEvent) {
        when(event) {
            is UserInfoEvent.SetUriForCrop -> setUriForCrop(event.uri)
            is UserInfoEvent.ValidateUserName -> validateUserName(event.name)
            is UserInfoEvent.ValidatePhone -> validatePhone(event.phone)
            is UserInfoEvent.SetCode -> setCode(event.code)
        }
    }

    fun handleUserLocationEvent(event: UserLocationEvent) {
        when(event) {
            UserLocationEvent.GetLocationFromAddress -> getLocationFromAddress()
            UserLocationEvent.RequestUserLocation -> requestCurrentLocation()
            is UserLocationEvent.UpdateUserAddress -> updateUserAddress(event.address)
        }
    }

    fun handleChildInfoEvent(event: ChildInfoEvent) {
        when(event) {
            ChildInfoEvent.SaveCurrentChild -> saveCurrentChild()
            is ChildInfoEvent.SetGender -> setGender(event.gender)
            is ChildInfoEvent.ValidateAge -> validateAge(event.age)
            is ChildInfoEvent.ValidateChildName -> validateChildName(event.name)
        }
    }

    fun handleChildrenInfoEvent(event: ChildrenInfoEvent) {
        when(event) {
            is ChildrenInfoEvent.DeleteChild -> deleteChild(event.id)
            ChildrenInfoEvent.ResetChild -> resetCurrentChild()
            is ChildrenInfoEvent.SetChild -> setCurrentChild(event.id)
        }
    }

    fun handleScheduleEvent(event: ScheduleEvent) {
        when(event) {
            ScheduleEvent.SetCurrentChildSchedule -> setCurrentChildSchedule()
            is ScheduleEvent.UpdateChildComment -> updateChildComment(event.comment)
            is ScheduleEvent.UpdateChildSchedule -> updateChildSchedule(event.day, event.period)
        }
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

    private fun setUriForCrop(uri: Uri) {
        uriForCrop = uri
    }

    private fun setCode(code: String) {
        _userInfoScreenState.update {
            it.copy(
                code = code
            )
        }
    }

    private fun validatePhone(phone: String) {
        userPhone = phone
        val phoneValid = if (phone.length in PHONE_LENGTH && phone.none { !it.isDigit() }) ValidField.VALID
        else ValidField.INVALID
        _userInfoScreenState.update {
            it.copy(
                phoneValid = phoneValid
            )
        }
    }

    private fun validateUserName(name: String) {
        userName = name
        val nameValid = if (name.length in NAME_LENGTH && name.all { it.isLetter() || it.isDigit() }) ValidField.VALID
        else ValidField.INVALID
        _userInfoScreenState.update {
            it.copy(
                nameValid = nameValid
            )
        }
    }

    private fun validateChildName(name: String) {
        childName = name
        val nameValid = if (name.none { !it.isLetter() }) ValidField.VALID
        else ValidField.INVALID
        _childInfoScreenState.update {
            it.copy(
                nameValid = nameValid
            )
        }
    }

    private fun validateAge(age: String) {
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

    private fun setGender(gender: Gender) {
        _childInfoScreenState.update {
            it.copy( gender = gender)
        }
    }

    private fun updateChildSchedule(dayOfWeek: DayOfWeek, dayPeriod: Period) {
        val currentSchedule = _childScheduleViewState.value.schedule
        when (dayPeriod) {
            Period.WHOLE_DAY -> {
                _childScheduleViewState.update {
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
                _childScheduleViewState.update {
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
                _childScheduleViewState.update {
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
                _childScheduleViewState.update {
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

        with(_childrenInfoViewState.value.children.toMutableList()) {
            val child = firstOrNull { it.id == currentChildId }
            if (child != null) {
                val index = indexOf(child)
                val newChild = child.copy(
                    schedule = _childScheduleViewState.value.schedule,
                )
                this[index] = newChild
            } else
                add(
                    Child(
                        id = currentChildId,
                        name = childName,
                        age = childAge,
                        gender = _childInfoScreenState.value.gender,
                        schedule = _childScheduleViewState.value.schedule,
                        comment = childComment.value
                    )
                )
            _childrenInfoViewState.update {
                it.copy(
                    children = this
                )
            }
        }
    }

    private fun updateChildComment(comment: String) {
        this.childComment.value = comment
    }

    private fun requestCurrentLocation() {
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

    private fun updateUserAddress(address: String) {
        this.userAddress = address
    }

    private fun getLocationFromAddress() {
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

    private fun getAddressFromLocation(latLng: LatLng) {
        networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess {  }
            onError {  }
            onLoading {  }
        }
    }

    private fun resetCurrentChild() {
        currentChildId = ""
    }

    private fun deleteChild(childId: String) {
        with(_childrenInfoViewState.value.children.toMutableList()) {
            val child = firstOrNull { it.id == childId }
            remove(child)
            _childrenInfoViewState.update {
                it.copy(
                    children = this
                )
            }
        }
    }

    private fun setCurrentChild(childId: String = "") {
        currentChildId = when {
            childId.isNotEmpty() -> childId
            currentChildId.isEmpty() -> UUID.randomUUID().toString()
            else -> currentChildId
        }
        val currentChild =
            _childrenInfoViewState.value.children.firstOrNull() { it.id == currentChildId }
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

    private fun saveCurrentChild() {
        val list = _childrenInfoViewState.value.children.toMutableList()
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
        _childrenInfoViewState.update {
            it.copy(
                children = list
            )
        }
    }

    private fun setCurrentChildSchedule() {
        with(_childrenInfoViewState.value.children.toMutableList()) {
            val child = firstOrNull { it.id == currentChildId }
            childComment.value = child?.comment.orEmpty()
            _childScheduleViewState.update {
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
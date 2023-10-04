package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.Child
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserLocationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildrenInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildrenInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.FullInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.FullProfileEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserLocationEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserLocationViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.decodeBase64
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onStart
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek
import java.util.UUID

class UserSettingsViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository,
    private val phoneNumberUtil: PhoneNumberUtil,
    private val bitmapHelper: BitmapHelper
): ViewModel() {

    private val _userInfoScreenState = MutableStateFlow(UserInfoViewState())
    val userInfoScreenState: StateFlow<UserInfoViewState> = _userInfoScreenState.asStateFlow()

    private val _locationScreenState = MutableStateFlow(UserLocationViewState())
    val locationScreenState: StateFlow<UserLocationViewState> = _locationScreenState.asStateFlow()

    private val _childInfoScreenState = MutableStateFlow(ChildInfoViewState())
    val childInfoScreenState: StateFlow<ChildInfoViewState> = _childInfoScreenState.asStateFlow()

    private val _childScheduleViewState = MutableStateFlow(ScheduleViewState())
    val childScheduleViewState: StateFlow<ScheduleViewState> = _childScheduleViewState.asStateFlow()

    private val _parentScheduleViewState = MutableStateFlow(ScheduleViewState())
    val parentScheduleViewState: StateFlow<ScheduleViewState> = _parentScheduleViewState.asStateFlow()

    private val _childrenInfoViewState = MutableStateFlow(ChildrenInfoViewState())
    val childrenInfoViewState: StateFlow<ChildrenInfoViewState> = _childrenInfoViewState.asStateFlow()

    private val _fullInfoViewState = MutableStateFlow(FullInfoViewState())
    val fullInfoViewState: StateFlow<FullInfoViewState> = _fullInfoViewState.asStateFlow()

    var childComment = mutableStateOf("")
        private set

    var parentComment = mutableStateOf("")
        private set

    private var currentChildId = ""

    private var uriForCrop: Uri = Uri.EMPTY

    init {
        getUserInfo()
    }

    fun handleUserInfoEvent(event: UserInfoEvent) {
        when(event) {
            is UserInfoEvent.SetImageToCrop -> setUriForCrop(event.uri)
            is UserInfoEvent.ValidateUserName -> validateUserName(event.name)
            is UserInfoEvent.ValidatePhone -> validatePhone(event.phone)
            is UserInfoEvent.SetCode -> setCode(event.code, event.country)
            UserInfoEvent.SaveInfo -> saveUserInfo()
            UserInfoEvent.ConsumeRequestError -> consumeUserInfoRequestError()
        }
    }

    fun handleUserLocationEvent(event: UserLocationEvent) {
        when(event) {
            UserLocationEvent.GetLocationFromAddress -> getLocationFromAddress()
            UserLocationEvent.RequestUserLocation -> requestCurrentLocation()
            is UserLocationEvent.UpdateUserAddress -> updateUserAddress(event.address)
            UserLocationEvent.ConsumeRequestError -> consumeUserLocationRequestError()
            UserLocationEvent.SaveUserLocation -> saveUserLocation()
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
            is ScheduleEvent.UpdateParentComment -> updateParentComment(event.comment)
            is ScheduleEvent.UpdateParentSchedule -> updateParentSchedule(event.day, event.period)
        }
    }

    fun handleFullProfileEvent(event: FullProfileEvent) {
        when(event) {
            FullProfileEvent.UpdateFullProfile -> updateFullProfile()
        }
    }

    fun getUserAvatarBitmap(): Bitmap {
        return bitmapHelper.bitmapFromUri(uriForCrop)
    }

    fun saveUserAvatar(image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val newImage = Bitmap.createScaledBitmap(image, 512, 512, true)
            val newImageSize = bitmapHelper.getSize(newImage)
            if (newImageSize < IMAGE_SIZE) {
                _userInfoScreenState.update {
                    it.copy(
                        userAvatar = image
                    )
                }
            } else {
                _userInfoScreenState.update {
                    it.copy(
                        avatarSizeError = triggered
                    )
                }
            }
        }
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity?> {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { entity ->
                _userInfoScreenState.update {
                    it.copy(
                        name = entity?.name.orEmpty(),
                        phone = entity?.phone.orEmpty(),
                        code = entity?.countryCode.orEmpty(),
                        userAvatar = entity?.avatar.orEmpty().decodeBase64(),
                        nameValid = if (!entity?.name.isNullOrEmpty()) ValidField.VALID else ValidField.EMPTY,
                        phoneValid = if (!entity?.phone.isNullOrEmpty()) ValidField.VALID else ValidField.EMPTY,
                    )
                }
                _locationScreenState.update {
                    it.copy(
                        currentLocation = LatLng(
                            entity?.location?.coordinates?.get(1) ?: 0.00,
                            entity?.location?.coordinates?.get(0) ?: 0.00
                        )
                    )
                }
                getAddressFromLocation(_locationScreenState.value.currentLocation)
            }
            onError { error ->
                _userInfoScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _userInfoScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun setUriForCrop(uri: Uri) {
        uriForCrop = uri
    }

    private fun setCode(code: String, country: String) {
        _userInfoScreenState.update {
            it.copy(
                code = code,
                country = country
            )
        }
    }

    private fun validatePhone(phone: String) {
        val phoneValid = try {
            val fullNumber = _userInfoScreenState.value.code + phone
            val phoneNumber = phoneNumberUtil.parse(fullNumber, _userInfoScreenState.value.country)
            if (phoneNumberUtil.isPossibleNumber(phoneNumber)) ValidField.VALID
            else ValidField.INVALID
        } catch (e: Exception) {
            ValidField.INVALID
        }
        _userInfoScreenState.update {
            it.copy(
                phone = phone,
                phoneValid = phoneValid
            )
        }
    }

    private fun validateUserName(name: String) {
        val nameValid = if (name.length in NAME_LENGTH && name.all { it.isLetter() || it.isDigit() }) ValidField.VALID
        else ValidField.INVALID
        _userInfoScreenState.update {
            it.copy(
                name =  name,
                nameValid = nameValid
            )
        }
    }

    private fun saveUserInfo() {
        networkExecutor {
            execute {
                userProfileRepository.saveUserInfo(
                    UserInfoEntity(
                        name = _userInfoScreenState.value.name,
                        phone = _userInfoScreenState.value.phone,
                        countryCode = _userInfoScreenState.value.code,
                        avatar = bitmapHelper.encodeToBase64(_userInfoScreenState.value.userAvatar)
                    )
                )
            }
            onSuccess {
                _userInfoScreenState.update {
                    it.copy(
                        requestSuccess = triggered
                    )
                }
            }
            onError { error ->
                _userInfoScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _userInfoScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun saveUserLocation() {
        networkExecutor {
            onStart {
                getAddressFromLocation(
                    LatLng(
                        _locationScreenState.value.currentLocation.latitude,
                        _locationScreenState.value.currentLocation.longitude
                    )
                )
            }
            execute {
                userProfileRepository.saveUserLocation(
                    UserLocationEntity(
                        lat = _locationScreenState.value.currentLocation.latitude,
                        lon = _locationScreenState.value.currentLocation.longitude
                    )
                )
            }
            onSuccess {
                _locationScreenState.update {
                    it.copy(
                        requestSuccess = triggered
                    )
                }
            }
            onError { error ->
                _locationScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _locationScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun consumeUserInfoRequestError() {
        _userInfoScreenState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }

    private fun consumeUserLocationRequestError() {
        _locationScreenState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }

    private fun validateChildName(name: String) {
        val nameValid = if (name.none { !it.isLetter() }) ValidField.VALID
        else ValidField.INVALID
        _childInfoScreenState.update {
            it.copy(
                name = name,
                nameValid = nameValid
            )
        }
    }

    private fun validateAge(age: String) {
        val intAge = age.toFloatOrNull()
        val ageValid = if (intAge != null && intAge in 0f..MAX_AGE) ValidField.VALID
        else ValidField.INVALID
        _childInfoScreenState.update {
            it.copy(
                age = age,
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
                        name = _childInfoScreenState.value.name,
                        age = _childInfoScreenState.value.age,
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

    private fun updateParentSchedule(dayOfWeek: DayOfWeek, dayPeriod: Period) {
        val currentSchedule = _parentScheduleViewState.value.schedule
        when (dayPeriod) {
            Period.WHOLE_DAY -> {
                _parentScheduleViewState.update {
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
                _parentScheduleViewState.update {
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
                _parentScheduleViewState.update {
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
                _parentScheduleViewState.update {
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

    private fun updateParentComment(comment: String) {
        this.parentComment.value = comment
    }

    private fun updateFullProfile() {
        _fullInfoViewState.update {
            it.copy(
                name = _userInfoScreenState.value.name,
                userAvatar = _userInfoScreenState.value.userAvatar,
                address = _locationScreenState.value.address,
                schedule = _parentScheduleViewState.value.schedule,
                children = _childrenInfoViewState.value.children,
                requestSuccess = consumed,
                requestError = consumed()
            )
        }
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
            onError { error ->
                _locationScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _locationScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun updateUserAddress(address: String) {
        _locationScreenState.update {
            it.copy(
                address = address
            )
        }
    }

    private fun getLocationFromAddress() {
        networkExecutor<LatLng?> {
            execute {
                locationRepository.getLocationFromAddress(_locationScreenState.value.address)
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
            onError { error ->
                _locationScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _locationScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getAddressFromLocation(latLng: LatLng) {
        networkExecutor<String?> {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess {
                updateUserAddress(it.orEmpty())
            }
        }
    }

    private fun resetCurrentChild() {
        currentChildId = ""
        _childInfoScreenState.update {
            ChildInfoViewState()
        }
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
            _childrenInfoViewState.value.children.firstOrNull { it.id == currentChildId }
                ?: Child(
                    id = currentChildId
                )
        _childInfoScreenState.update {
            it.copy(
                name = currentChild.name,
                nameValid = ValidField.VALID,
                age = currentChild.age,
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
                name = _childInfoScreenState.value.name,
                age = _childInfoScreenState.value.age,
                gender = _childInfoScreenState.value.gender
            )
            list[index] = newChild
        } else {
            currentChildId = UUID.randomUUID().toString()
            list.add(
                Child(
                    id = currentChildId,
                    name = _childInfoScreenState.value.name,
                    age = _childInfoScreenState.value.age,
                    gender = _childInfoScreenState.value.gender
                )
            )
        }
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

        private val NAME_LENGTH = 6..18
        private const val MAX_AGE = 18f
        private const val IMAGE_SIZE = 500 * 1024
    }
}
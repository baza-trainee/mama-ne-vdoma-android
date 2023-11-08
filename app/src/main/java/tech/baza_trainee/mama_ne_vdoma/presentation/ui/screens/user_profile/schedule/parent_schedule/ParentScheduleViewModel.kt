package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.updateSchedule
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserProfileInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import java.time.DayOfWeek

class ParentScheduleViewModel(
    private val communicator: UserProfileCommunicator,
    private val navigator: ScreenNavigator,
    private val userProfileInteractor: UserProfileInteractor,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel(), UserProfileInteractor by userProfileInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(ScheduleViewState())
    val viewState: StateFlow<ScheduleViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        userProfileInteractor.apply {
            setUserProfileCoroutineScope(viewModelScope)
            setUserProfileNetworkListener(this@ParentScheduleViewModel)
        }

        _viewState.update {
            it.copy(
                schedule = communicator.schedule,
                comment = preferencesDatastoreManager.note
            )
        }
    }

    override fun onLoading(state: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = state
            )
        }
    }

    override fun onError(error: String) {
        _uiState.value = RequestState.OnError(error)
    }

    fun handleScheduleEvent(event: ScheduleEvent) {
        when(event) {
            ScheduleEvent.OnBack -> navigator.goBack()
            ScheduleEvent.PatchParentSchedule -> saveParentSchedule()
            ScheduleEvent.ResetUiState -> _uiState.value = RequestState.Idle
            is ScheduleEvent.UpdateParentSchedule -> updateParentSchedule(event.day, event.period)
            is ScheduleEvent.UpdateParentComment -> updateComment(event.comment)
            else -> Unit
        }
    }

    private fun saveParentSchedule() {
        updateParent(
            UserInfoEntity(
                name = preferencesDatastoreManager.name,
                phone = preferencesDatastoreManager.phone,
                countryCode = preferencesDatastoreManager.code,
                avatar = preferencesDatastoreManager.avatar,
                schedule = _viewState.value.schedule,
                note = _viewState.value.comment
            )
        ) {
            preferencesDatastoreManager.isUserProfileFilled = true
            navigator.navigate(UserProfileRoutes.FullProfile)
        }
    }

    private fun updateParentSchedule(dayOfWeek: DayOfWeek, dayPeriod: Period) {
        _viewState.update {
            it.copy(
                schedule = it.schedule.updateSchedule(dayOfWeek, dayPeriod)
            )
        }
    }

    private fun updateComment(value: String) {
        val fieldValid = if (value.length < 1000) ValidField.VALID else ValidField.INVALID
        _viewState.update {
            it.copy(
                comment = value,
                commentValid = fieldValid
            )
        }
    }
}
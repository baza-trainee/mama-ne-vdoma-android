package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserProfileInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import java.time.DayOfWeek

class ChildScheduleViewModel(
    private val communicator: UserProfileCommunicator,
    private val navigator: ScreenNavigator,
    private val userProfileInteractor: UserProfileInteractor
): ViewModel(), UserProfileInteractor by userProfileInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(ScheduleViewState())
    val viewState: StateFlow<ScheduleViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        userProfileInteractor.apply {
            setUserProfileCoroutineScope(viewModelScope)
            setUserProfileNetworkListener(this@ChildScheduleViewModel)
        }

        if (communicator.currentChildId.isNotEmpty())
            getChildById()
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
            ScheduleEvent.OnBack -> navigator.navigate(UserProfileRoutes.FullProfile)
            ScheduleEvent.PatchChildSchedule -> patchChild()
            ScheduleEvent.ResetUiState -> _uiState.value = RequestState.Idle
            is ScheduleEvent.UpdateChildComment -> updateChildComment(event.comment)
            is ScheduleEvent.UpdateChildSchedule -> updateChildSchedule(event.day, event.period)
            else -> Unit
        }
    }

    private fun updateChildSchedule(dayOfWeek: DayOfWeek, dayPeriod: Period) {
        _viewState.update {
            it.copy(
                schedule = updateSchedule(it.schedule, dayOfWeek, dayPeriod)
            )
        }
    }

    private fun updateChildComment(comment: String) {
        val fieldValid = if (comment.length < 1000) ValidField.VALID else ValidField.INVALID
        _viewState.update {
            it.copy(
                comment = comment,
                commentValid = fieldValid
            )
        }
    }

    private fun getChildById() {
        getChildById(communicator.currentChildId) { entity ->
            _viewState.update {
                it.copy(
                    schedule = entity?.schedule ?: ScheduleModel()
                )
            }
        }
    }

    private fun patchChild() {
        patchChild(communicator.currentChildId, _viewState.value.comment, _viewState.value.schedule) {
            communicator.isChildInfoFilled = true
            navigator.navigateOnMain(viewModelScope, UserProfileRoutes.FullProfile)
        }
    }
}
package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.child_schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.getDefaultSchedule
import tech.baza_trainee.mama_ne_vdoma.domain.model.updateSchedule
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.NetworkEventsListener
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserProfileInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import java.time.DayOfWeek

class ChildScheduleViewModel(
    private val nextRoute: () -> Unit,
    private val backRoute: () -> Unit,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val userProfileInteractor: UserProfileInteractor
): ViewModel(), UserProfileInteractor by userProfileInteractor, NetworkEventsListener {

    private val _viewState = MutableStateFlow(ScheduleViewState())
    val viewState: StateFlow<ScheduleViewState> = _viewState.asStateFlow()

    private val _uiState = MutableStateFlow<RequestState>(RequestState.Idle)
    val uiState: StateFlow<RequestState>
        get() = _uiState.asStateFlow()

    init {
        userProfileInteractor.apply {
            setUserProfileCoroutineScope(viewModelScope)
            setUserProfileNetworkListener(this@ChildScheduleViewModel)
        }

        if (preferencesDatastoreManager.currentChild.isNotEmpty())
            getChildById(preferencesDatastoreManager.currentChild)
    }

    override fun onLoading(state: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = state
            )
        }
    }

    override fun onError(error: String) {
        _uiState.update { RequestState.OnError(error) }
    }

    fun handleScheduleEvent(event: ScheduleEvent) {
        when(event) {
            ScheduleEvent.OnBack -> backRoute()
            ScheduleEvent.PatchChildSchedule -> patchChild()
            ScheduleEvent.ResetUiState -> _uiState.update { RequestState.Idle }
            is ScheduleEvent.UpdateChildComment -> updateChildComment(event.comment)
            is ScheduleEvent.UpdateChildSchedule -> updateChildSchedule(event.day, event.period)
            else -> Unit
        }
    }

    private fun updateChildSchedule(dayOfWeek: DayOfWeek, dayPeriod: Period) {
        _viewState.update {
            it.copy(
                schedule = it.schedule.updateSchedule(dayOfWeek, dayPeriod)
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

    private fun getChildById(childId: String) {
        getChildById(childId) { entity ->
            _viewState.update {
                it.copy(
                    schedule = entity?.schedule ?: getDefaultSchedule()
                )
            }
        }
    }

    private fun patchChild() {
        patchChild(preferencesDatastoreManager.currentChild, _viewState.value.comment, _viewState.value.schedule) {
            preferencesDatastoreManager.isChildrenDataProvided = true
            nextRoute()
        }
    }
}
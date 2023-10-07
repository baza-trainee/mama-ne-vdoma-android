package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.PatchChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek

class ChildScheduleViewModel(
    private val communicator: UserProfileCommunicator,
    private val userProfileRepository: UserProfileRepository
): ViewModel() {

    private val _childScheduleViewState = MutableStateFlow(ScheduleViewState())
    val childScheduleViewState: StateFlow<ScheduleViewState> = _childScheduleViewState.asStateFlow()

    var childComment = mutableStateOf("")
        private set

    init {
        if (communicator.currentChildId.isNotEmpty())
            getChildById()
    }

    fun handleScheduleEvent(event: ScheduleEvent) {
        when(event) {
            ScheduleEvent.PatchChildSchedule -> patchChild()
            ScheduleEvent.ConsumeRequestError -> consumeRequestError()
            ScheduleEvent.ConsumeRequestSuccess -> consumeRequestSuccess()
            is ScheduleEvent.UpdateChildComment -> updateChildComment(event.comment)
            is ScheduleEvent.UpdateChildSchedule -> updateChildSchedule(event.day, event.period)
            else -> Unit
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

//        with(communicator.children.toMutableList()) {
//            val child = firstOrNull { it.id == communicator.currentChild.id }
//            if (child != null) {
//                val index = indexOf(child)
//                val newChild = child.copy(
//                    schedule = _childScheduleViewState.value.schedule,
//                    comment = childComment.value
//                )
//                this[index] = newChild
//            }
//            communicator.children = this
//        }
    }

    private fun updateChildComment(comment: String) {
        this.childComment.value = comment
    }

    private fun getChildById() {
        networkExecutor<ChildEntity?> {
            execute {
                userProfileRepository.getChildById(communicator.currentChildId)
            }
            onSuccess { entity ->
                _childScheduleViewState.update {
                    it.copy(
                        schedule = entity?.schedule ?: ScheduleModel()
                    )
                }
            }
            onError { error ->
                _childScheduleViewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _childScheduleViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun patchChild() {
        networkExecutor {
            execute {
                userProfileRepository.patchChildById(
                    communicator.currentChildId,
                    PatchChildEntity(
                        comment = childComment.value,
                        schedule = _childScheduleViewState.value.schedule
                    )
                )
            }
            onSuccess {
                communicator.isChildInfoFilled = true
                _childScheduleViewState.update {
                    it.copy(
                        requestSuccess = triggered
                    )
                }
            }
            onError { error ->
                _childScheduleViewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _childScheduleViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun consumeRequestError() {
        _childScheduleViewState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }

    private fun consumeRequestSuccess() {
        _childScheduleViewState.update {
            it.copy(
                requestSuccess = consumed
            )
        }
    }
}
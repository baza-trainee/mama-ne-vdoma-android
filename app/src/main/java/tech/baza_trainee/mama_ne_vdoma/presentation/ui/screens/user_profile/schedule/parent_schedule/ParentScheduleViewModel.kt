package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule

import androidx.lifecycle.ViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.ScheduleViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek

class ParentScheduleViewModel(
    private val communicator: UserProfileCommunicator,
    private val userProfileRepository: UserProfileRepository,
    private val bitmapHelper: BitmapHelper
): ViewModel() {

    private val _parentScheduleViewState = MutableStateFlow(ScheduleViewState())
    val parentScheduleViewState: StateFlow<ScheduleViewState> = _parentScheduleViewState.asStateFlow()

    init {
        _parentScheduleViewState.update {
            it.copy(
                schedule = communicator.schedule
            )
        }
    }

    fun handleScheduleEvent(event: ScheduleEvent) {
        when(event) {
            ScheduleEvent.ConsumeRequestError -> consumeRequestError()
            ScheduleEvent.ConsumeRequestSuccess -> consumeRequestSuccess()
            ScheduleEvent.PatchParentSchedule -> saveParentSchedule()
            is ScheduleEvent.UpdateParentSchedule -> updateParentSchedule(event.day, event.period)
            else -> Unit
        }
    }

    private fun saveParentSchedule() {
        networkExecutor {
            execute {
                userProfileRepository.saveUserInfo(
                    UserInfoEntity(
                        name = communicator.name,
                        phone = communicator.phone,
                        countryCode = communicator.code,
                        avatar = bitmapHelper.encodeToBase64(communicator.userAvatar),
                        schedule = _parentScheduleViewState.value.schedule
                    )
                )
            }
            onSuccess {
                communicator.isUserInfoFilled = true
                _parentScheduleViewState.update {
                    it.copy(
                        requestSuccess = triggered
                    )
                }
            }
            onError { error ->
                _parentScheduleViewState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _parentScheduleViewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
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

    private fun consumeRequestSuccess() {
        _parentScheduleViewState.update {
            it.copy(
                requestSuccess = consumed
            )
        }
    }

    private fun consumeRequestError() {
        _parentScheduleViewState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }
}
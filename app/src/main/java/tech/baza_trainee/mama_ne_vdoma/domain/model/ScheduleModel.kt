package tech.baza_trainee.mama_ne_vdoma.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.parcelize.Parcelize
import tech.baza_trainee.mama_ne_vdoma.R
import java.time.DayOfWeek

fun getDefaultSchedule() = mutableStateMapOf<DayOfWeek, DayPeriod>().also { map ->
    DayOfWeek.values().forEach {
        map[it] = DayPeriod()
    }
}

fun Map<DayOfWeek, DayPeriod>.updateSchedule(dayOfWeek: DayOfWeek, dayPeriod: Period): SnapshotStateMap<DayOfWeek, DayPeriod> {
    toMutableMap().apply {
        val map =  when (dayPeriod) {
            Period.WHOLE_DAY -> {
                apply {
                    this[dayOfWeek] = this[dayOfWeek]?.copy(
                        wholeDay = this[dayOfWeek]?.wholeDay?.not() ?: false
                    ) ?: DayPeriod()
                    if (this[dayOfWeek]?.wholeDay == true) {
                        this[dayOfWeek] = this[dayOfWeek]?.copy(
                            morning = false,
                            noon = false,
                            afternoon = false
                        ) ?: DayPeriod()
                    }
                }
            }

            Period.MORNING -> {
                apply {
                    this[dayOfWeek] = this[dayOfWeek]?.copy(
                        morning = this[dayOfWeek]?.morning?.not() ?: false
                    ) ?: DayPeriod()
                    if (this[dayOfWeek]?.morning == true &&
                        this[dayOfWeek]?.noon == true &&
                        this[dayOfWeek]?.afternoon == true
                    ) {
                        this[dayOfWeek] = this[dayOfWeek]?.copy(
                            wholeDay = true,
                            morning = false,
                            noon = false,
                            afternoon = false
                        ) ?: DayPeriod()
                    } else if (this[dayOfWeek]?.morning == true && this[dayOfWeek]?.wholeDay == true) {
                        this[dayOfWeek] = this[dayOfWeek]?.copy(
                            wholeDay = false
                        ) ?: DayPeriod()
                    }
                }
            }

            Period.NOON -> {
                apply {
                    this[dayOfWeek] = this[dayOfWeek]?.copy(
                        noon = this[dayOfWeek]?.noon?.not() ?: false
                    ) ?: DayPeriod()
                    if (this[dayOfWeek]?.morning == true &&
                        this[dayOfWeek]?.noon == true &&
                        this[dayOfWeek]?.afternoon == true
                    ) {
                        this[dayOfWeek] = this[dayOfWeek]?.copy(
                            wholeDay = true,
                            morning = false,
                            noon = false,
                            afternoon = false
                        ) ?: DayPeriod()
                    } else if (this[dayOfWeek]?.noon == true && this[dayOfWeek]?.wholeDay == true) {
                        this[dayOfWeek] = this[dayOfWeek]?.copy(
                            wholeDay = false
                        ) ?: DayPeriod()
                    }
                }
            }

            Period.AFTERNOON -> {
                apply {
                    this[dayOfWeek] = this[dayOfWeek]?.copy(
                        afternoon = this[dayOfWeek]?.afternoon?.not() ?: false
                    ) ?: DayPeriod()
                    if (this[dayOfWeek]?.morning == true &&
                        this[dayOfWeek]?.noon == true &&
                        this[dayOfWeek]?.afternoon == true
                    ) {
                        this[dayOfWeek] = this[dayOfWeek]?.copy(
                            wholeDay = true,
                            morning = false,
                            noon = false,
                            afternoon = false
                        ) ?: DayPeriod()
                    } else if (this[dayOfWeek]?.afternoon == true && this[dayOfWeek]?.wholeDay == true) {
                        this[dayOfWeek] = this[dayOfWeek]?.copy(
                            wholeDay = false
                        ) ?: DayPeriod()
                    }
                }
            }
        }
        return mutableStateMapOf<DayOfWeek, DayPeriod>().apply { putAll(map) }
    }
}

@Parcelize
data class DayPeriod(
    val morning: Boolean = false,
    val noon: Boolean = false,
    val afternoon: Boolean = false,
    val wholeDay: Boolean = false
): Parcelable {

    fun isFilled() = morning || noon || afternoon || wholeDay
}

enum class Period(@StringRes val period: Int) {
    MORNING(R.string.morning), NOON(R.string.noon), AFTERNOON(R.string.evening), WHOLE_DAY(R.string.whole_day)
}

package tech.baza_trainee.mama_ne_vdoma.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import tech.baza_trainee.mama_ne_vdoma.R
import java.time.DayOfWeek

fun getDefaultSchedule() = hashMapOf<DayOfWeek, DayPeriod>().also { map ->
    DayOfWeek.entries.forEach {
        map[it] = DayPeriod()
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

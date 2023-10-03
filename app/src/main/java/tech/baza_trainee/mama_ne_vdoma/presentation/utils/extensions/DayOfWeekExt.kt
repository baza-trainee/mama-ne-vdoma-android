package tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

fun DayOfWeek.short(locale: Locale = Locale.getDefault()) = getDisplayName(TextStyle.SHORT, locale).lowercase(locale)
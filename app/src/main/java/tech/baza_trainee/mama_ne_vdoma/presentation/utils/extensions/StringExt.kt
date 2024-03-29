package tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import tech.baza_trainee.mama_ne_vdoma.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.regex.Pattern
import kotlin.math.abs

fun String?.orDefault(default: String) = orEmpty().ifEmpty { default }

fun String.validateEmail(): Boolean = Pattern.compile(EMAIL_PATTERN).matcher(this).matches()

fun String.validatePassword() = contains(PATTERN_DIGITS.toRegex()) &&
        contains(PATTERN_LATIN.toRegex()) &&
        filterNot { it.isLetter() || it.isDigit() }.contains(PATTERN_SPECIAL_CHARACTERS.toRegex()) &&
        none { it.isWhitespace() } && !none { it.isLowerCase() } && !none { it.isUpperCase() } &&
        length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH

fun String.validateName(lengthRange: IntRange) = length in lengthRange &&
        all { it.isLetter() || it.isDigit() || it == ' ' || it == '-' || it == '\'' }

fun String.decodeBase64ToBitmap(): Bitmap {
    val decodedByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
}

fun String.encodeToBase64(): String {
    val byteArray = toByteArray(Charsets.UTF_8)
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun String.decodeFromBase64(): String {
    val byteArray = Base64.decode(this, Base64.DEFAULT)
    return String(byteArray, Charsets.UTF_8)
}

fun String.capitalize(locale: Locale = Locale.getDefault()) =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

fun String.formatDate(): String {
    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val localDateTime = LocalDateTime.parse(this, inputFormat)

    return outputFormat.format(localDateTime)
}

fun String.formatTime(): String {
    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = DateTimeFormatter.ofPattern("HH:mm")
    val localDateTime = LocalDateTime.parse(this, inputFormat)

    return outputFormat.format(localDateTime)
}

fun String.getMessageDate(context: Context): String {
    if (isEmpty()) return ""

    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val parsedDateTime = LocalDateTime.parse(this, inputFormat)
    val currentDateTime = LocalDateTime.now()

    val diffMinutes = abs(java.time.Duration.between(parsedDateTime, currentDateTime).toMinutes())

    return when {
        diffMinutes <= 5 -> context.getString(R.string.right_now)
        parsedDateTime.toLocalDate() == currentDateTime.toLocalDate() -> context.getString(R.string.today)
        parsedDateTime.plusDays(1).toLocalDate() == currentDateTime.toLocalDate() -> context.getString(R.string.yesterday)
        parsedDateTime.year == currentDateTime.year -> parsedDateTime.format(DateTimeFormatter.ofPattern("dd MMM"))
        else -> parsedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}

const val EMAIL_PATTERN =
    "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})\$"

const val MIN_PASSWORD_LENGTH = 6
const val MAX_PASSWORD_LENGTH = 24
const val PATTERN_DIGITS = "[0-9]"
const val PATTERN_LATIN = "[a-zA-Z]"
const val PATTERN_SPECIAL_CHARACTERS = "[!@#%^&*()_+-=\\[\\]{}|;':\\\",./<>?~`]"
package tech.baza_trainee.mama_ne_vdoma.presentation.utils

sealed class RequestResult<out T : Any?> {
    data class Success<out T : Any?>(val result: T) : RequestResult<T>()
    data class Error(val error: String, val code: Int = 0) : RequestResult<Nothing>()
}
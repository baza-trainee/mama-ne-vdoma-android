package tech.baza_trainee.mama_ne_vdoma.presentation.utils

sealed interface RequestState {
    object Idle: RequestState
    object Success: RequestState
    data class Error(val errorMessage: String): RequestState
}
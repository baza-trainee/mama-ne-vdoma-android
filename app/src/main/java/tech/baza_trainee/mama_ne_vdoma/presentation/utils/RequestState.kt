package tech.baza_trainee.mama_ne_vdoma.presentation.utils

sealed interface RequestState {
    data object Idle: RequestState
    data class OnError(val error: String): RequestState
}

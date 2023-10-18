package tech.baza_trainee.mama_ne_vdoma.presentation.interactors

interface NetworkEventsListener {
    fun onLoading(state: Boolean)
    fun onError(error: String)
}
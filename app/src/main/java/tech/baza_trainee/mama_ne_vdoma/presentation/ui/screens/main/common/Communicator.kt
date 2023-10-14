package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Communicator {

    private val _pageFlow = MutableSharedFlow<Int>()
    val pageFlow: SharedFlow<Int> = _pageFlow.asSharedFlow()

    fun setPage(value: Int) {
        _pageFlow.tryEmit(value)
    }
}
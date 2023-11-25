package tech.baza_trainee.mama_ne_vdoma.presentation.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Communicator<T>(private val initial: T) {

    private val _dataFlow = MutableStateFlow(initial)
    val dataFlow: StateFlow<T> = _dataFlow.asStateFlow()

    fun setData(value: T) {
        _dataFlow.update {
            value
        }
    }
}
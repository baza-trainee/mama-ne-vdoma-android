package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.Child
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildInfoEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.ChildInfoViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import java.util.UUID

class ChildInfoViewModel(
    private val communicator: UserProfileCommunicator
): ViewModel() {

    private val _childInfoScreenState = MutableStateFlow(ChildInfoViewState())
    val childInfoScreenState: StateFlow<ChildInfoViewState> = _childInfoScreenState.asStateFlow()

    init {
        _childInfoScreenState.update {
            it.copy(
                name = communicator.currentChild.name,
                nameValid = ValidField.VALID,
                age = communicator.currentChild.age,
                ageValid = ValidField.VALID,
                gender = communicator.currentChild.gender
            )
        }
    }

    fun handleChildInfoEvent(event: ChildInfoEvent) {
        when(event) {
            ChildInfoEvent.SaveCurrentChild -> saveCurrentChild()
            is ChildInfoEvent.SetGender -> setGender(event.gender)
            is ChildInfoEvent.ValidateAge -> validateAge(event.age)
            is ChildInfoEvent.ValidateChildName -> validateChildName(event.name)
        }
    }

    private fun validateChildName(name: String) {
        val nameValid = if (name.none { !it.isLetter() }) ValidField.VALID
        else ValidField.INVALID
        _childInfoScreenState.update {
            it.copy(
                name = name,
                nameValid = nameValid
            )
        }
    }

    private fun validateAge(age: String) {
        val intAge = age.toFloatOrNull()
        val ageValid = if (intAge != null && intAge in 0f..MAX_AGE) ValidField.VALID
        else ValidField.INVALID
        _childInfoScreenState.update {
            it.copy(
                age = age,
                ageValid = ageValid
            )
        }
    }

    private fun setGender(gender: Gender) {
        _childInfoScreenState.update {
            it.copy( gender = gender)
        }
    }

    private fun saveCurrentChild() {
        val list = communicator.children.toMutableList()
        val child = list.firstOrNull { it.id == communicator.currentChild.id }
        if (child != null) {
            val index = list.indexOf(child)
            val newChild = child.copy(
                name = _childInfoScreenState.value.name,
                age = _childInfoScreenState.value.age,
                gender = _childInfoScreenState.value.gender
            )
            list[index] = newChild
        } else {
            communicator.currentChild = Child (
                id = UUID.randomUUID().toString(),
                name = _childInfoScreenState.value.name,
                age = _childInfoScreenState.value.age,
                gender = _childInfoScreenState.value.gender
            )
            list.add(communicator.currentChild)
        }
        communicator.children = list
    }

    companion object {

        private const val MAX_AGE = 18f
    }
}
package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.child_info

import androidx.lifecycle.ViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.domain.model.InitChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class ChildInfoViewModel(
    private val communicator: UserProfileCommunicator,
    private val userProfileRepository: UserProfileRepository
): ViewModel() {

    private val _childInfoScreenState = MutableStateFlow(ChildInfoViewState())
    val childInfoScreenState: StateFlow<ChildInfoViewState> = _childInfoScreenState.asStateFlow()

    fun handleChildInfoEvent(event: ChildInfoEvent) {
        when(event) {
            ChildInfoEvent.SaveChild -> saveChild()
            is ChildInfoEvent.SetGender -> setGender(event.gender)
            is ChildInfoEvent.ValidateAge -> validateAge(event.age)
            is ChildInfoEvent.ValidateChildName -> validateChildName(event.name)
            ChildInfoEvent.ConsumeRequestError -> consumeRequestError()
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
        val intAge = age.toIntOrNull()
        val ageValid = if (intAge != null && intAge in 0..MAX_AGE) ValidField.VALID
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

    private fun saveChild() {
        networkExecutor<ChildEntity?> {
            execute {
                userProfileRepository.saveChild(
                    InitChildEntity(
                        name = _childInfoScreenState.value.name,
                        age = _childInfoScreenState.value.age.toIntOrNull() ?: 0,
                        isMale = _childInfoScreenState.value.gender == Gender.BOY
                    )
                )
            }
            onSuccess { entity ->
                communicator.currentChildId = entity?.childId.orEmpty()
                _childInfoScreenState.update {
                    it.copy(
                        requestSuccess = triggered
                    )
                }
            }
            onError { error ->
                _childInfoScreenState.update {
                    it.copy(
                        requestError = triggered(error)
                    )
                }
            }
            onLoading { isLoading ->
                _childInfoScreenState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun consumeRequestError() {
        _childInfoScreenState.update {
            it.copy(
                requestError = consumed()
            )
        }
    }

    companion object {

        private const val MAX_AGE = 18
    }
}
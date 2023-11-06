package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class ChildInfoViewModel(
    private val nextRoute: () -> Unit,
    private val backRoute: () -> Unit,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val userProfileRepository: UserProfileRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(ChildInfoViewState())
    val viewState: StateFlow<ChildInfoViewState> = _viewState.asStateFlow()

    private val _events = Channel<RequestState>()
    val events: Flow<RequestState> = _events.receiveAsFlow()

    fun handleChildInfoEvent(event: ChildInfoEvent) {
        when(event) {
            ChildInfoEvent.SaveChild -> saveChild()
            is ChildInfoEvent.SetGender -> setGender(event.gender)
            is ChildInfoEvent.ValidateAge -> validateAge(event.age)
            is ChildInfoEvent.ValidateChildName -> validateChildName(event.name)
            ChildInfoEvent.OnBack -> backRoute()
        }
    }

    private fun validateChildName(name: String) {
        val nameValid = if (name.length in NAME_LENGTH &&
            name.all { it.isLetter() || it.isDigit() || it == ' ' || it == '-' })
            ValidField.VALID
        else
            ValidField.INVALID
        _viewState.update {
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
        _viewState.update {
            it.copy(
                age = age,
                ageValid = ageValid
            )
        }
    }

    private fun setGender(gender: Gender) {
        _viewState.update {
            it.copy( gender = gender)
        }
    }

    private fun saveChild() {
        networkExecutor<ChildEntity?> {
            execute {
                userProfileRepository.saveChild(
                    name = _viewState.value.name,
                    age = _viewState.value.age.toIntOrNull() ?: 0,
                    isMale = _viewState.value.gender == Gender.BOY
                )
            }
            onSuccess { entity ->
                preferencesDatastoreManager.currentChild = entity?.childId.orEmpty()
                nextRoute()
            }
            onError { error ->
                _events.trySend(RequestState.OnError(error))
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    companion object {

        private val NAME_LENGTH = 2..18
        private const val MAX_AGE = 18
    }
}
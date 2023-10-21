package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.child_info

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.domain.model.InitChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class ChildInfoViewModel(
    private val communicator: UserProfileCommunicator,
    private val navigator: ScreenNavigator,
    private val userProfileRepository: UserProfileRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(ChildInfoViewState())
    val viewState: StateFlow<ChildInfoViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    fun handleChildInfoEvent(event: ChildInfoEvent) {
        when(event) {
            ChildInfoEvent.SaveChild -> saveChild()
            ChildInfoEvent.ResetUiState -> _uiState.value = RequestState.Idle
            is ChildInfoEvent.SetGender -> setGender(event.gender)
            is ChildInfoEvent.ValidateAge -> validateAge(event.age)
            is ChildInfoEvent.ValidateChildName -> validateChildName(event.name)
            ChildInfoEvent.OnBack -> navigator.navigate(UserProfileRoutes.FullProfile)
        }
    }

    private fun validateChildName(name: String) {
        val nameValid = if (name.none { !it.isLetter() }) ValidField.VALID
        else ValidField.INVALID
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
                    InitChildEntity(
                        name = _viewState.value.name,
                        age = _viewState.value.age.toIntOrNull() ?: 0,
                        isMale = _viewState.value.gender == Gender.BOY
                    )
                )
            }
            onSuccess { entity ->
                communicator.currentChildId = entity?.childId.orEmpty()
                navigator.navigateOnMain(viewModelScope, UserProfileRoutes.ChildSchedule)
            }
            onError { error ->
                _uiState.value = RequestState.OnError(error)
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

        private const val MAX_AGE = 18
    }
}
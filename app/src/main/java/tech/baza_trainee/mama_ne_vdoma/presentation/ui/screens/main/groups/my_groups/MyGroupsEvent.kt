package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups

sealed interface MyGroupsEvent {
    data object OnBack: MyGroupsEvent
    data object ResetUiState : MyGroupsEvent
    data object CreateNewGroup: MyGroupsEvent
}
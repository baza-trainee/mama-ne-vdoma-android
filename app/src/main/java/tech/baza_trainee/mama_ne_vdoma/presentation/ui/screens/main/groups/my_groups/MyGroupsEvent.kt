package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups

sealed interface MyGroupsEvent {
    data object OnBack: MyGroupsEvent
    data object ResetUiState : MyGroupsEvent
    data object CreateNewGroup: MyGroupsEvent
    data class OnLeave(val group: String): MyGroupsEvent
    data class OnEdit(val group: String): MyGroupsEvent
    data class OnDelete(val group: String): MyGroupsEvent
    data class OnSwitchAdmin(val group: String, val member: String): MyGroupsEvent
}
package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupsInSearchUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.ParentInSearchUiModel

class SearchResultsCommunicator {
    var user: ParentInSearchUiModel = ParentInSearchUiModel()
    var groups: List<GroupsInSearchUiModel> = emptyList()
    var avatarId: String = ""
}
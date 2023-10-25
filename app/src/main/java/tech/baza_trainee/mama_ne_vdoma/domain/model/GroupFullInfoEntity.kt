package tech.baza_trainee.mama_ne_vdoma.domain.model

data class GroupFullInfoEntity(
    val group: GroupEntity = GroupEntity(),
    val parents: List<UserProfileEntity> = emptyList(),
    val children: List<ChildEntity> = emptyList()
)

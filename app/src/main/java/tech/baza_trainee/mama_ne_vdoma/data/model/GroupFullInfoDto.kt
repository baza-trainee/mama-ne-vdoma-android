package tech.baza_trainee.mama_ne_vdoma.data.model

import com.google.gson.annotations.SerializedName

data class GroupFullInfoDto(
    val group: GroupDto,
    val parents: List<UserProfileDto>,
    @SerializedName("childs") val children: List<ChildDto>
)

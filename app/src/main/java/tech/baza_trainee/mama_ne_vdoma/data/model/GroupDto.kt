package tech.baza_trainee.mama_ne_vdoma.data.model

import com.google.gson.annotations.SerializedName

data class GroupDto(
    val name: String,
    val desc: String,
    val adminId: String,
    val ages: String,
    val members: List<MemberDto>,
    val location: LocationDto,
    val askingJoin: List<MemberDto>,
    val week: Map<String, DayScheduleDto>?,
    @SerializedName("_id")
    val id: String
)

data class MemberDto(
    val childId: String,
    val parentId: String
)
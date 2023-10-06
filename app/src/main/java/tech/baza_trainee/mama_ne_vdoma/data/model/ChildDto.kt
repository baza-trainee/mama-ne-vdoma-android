package tech.baza_trainee.mama_ne_vdoma.data.model

import com.google.gson.annotations.SerializedName

data class ChildDto(
    val name: String,
    val age: Int,
    val isMale: Boolean,
    val note: String,
    val parentId: String,
    @SerializedName("_id")
    val childId: String
)

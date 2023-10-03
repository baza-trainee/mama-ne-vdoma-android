package tech.baza_trainee.mama_ne_vdoma.data.model

data class RestorePasswordDto(
    val email: String = "",
    val code: String = "",
    val password: String = ""
)
package tech.baza_trainee.mama_ne_vdoma.domain.model

data class RestorePasswordEntity(
    val email: String = "",
    val code: String = "",
    val password: String = ""
)
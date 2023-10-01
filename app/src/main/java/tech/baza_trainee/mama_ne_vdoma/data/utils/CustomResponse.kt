package tech.baza_trainee.mama_ne_vdoma.data.utils

data class CustomResponse(
    val message: List<String> = emptyList(),
    val error: String = "",
    val statusCode: Int = 0
)

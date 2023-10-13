package tech.baza_trainee.mama_ne_vdoma.data.model

data class LocationDto(
    val type: String = "",
    val coordinates: List<Double> = emptyList()
)
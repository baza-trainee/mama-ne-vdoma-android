package tech.baza_trainee.mama_ne_vdoma.domain.model

data class LocationEntity(
    val type: String = "",
    val coordinates: List<Double> = emptyList()
)
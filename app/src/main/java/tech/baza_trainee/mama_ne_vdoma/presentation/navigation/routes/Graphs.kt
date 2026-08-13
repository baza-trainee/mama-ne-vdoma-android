package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable

sealed interface Graphs : CommonRoute {

    @Serializable
    data object Start : Graphs

    @Serializable
    data object CreateUser : Graphs

    @Serializable
    data object Login : Graphs

    @Serializable
    data object UserProfile : Graphs

    @Serializable
    data object FirstGroupSearch : Graphs

    @Serializable
    data object Host : Graphs

    sealed interface HostNested : Graphs {

        @Serializable
        data object Main : HostNested

        @Serializable
        data object Groups : HostNested

        @Serializable
        data object Chat : HostNested

        @Serializable
        data object Search : HostNested

        @Serializable
        data object Settings : HostNested
    }
}

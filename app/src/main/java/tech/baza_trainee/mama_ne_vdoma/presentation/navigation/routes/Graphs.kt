package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class Graphs(val route: String): CommonRoute(route) {
    object Start : Graphs("start_graph")
    object CreateUser : Graphs("create_user_graph")
    object Login : Graphs("login_graph")
    object UserProfile : Graphs("user_profile_graph")
    object Main : Graphs("main_graph")
    object GroupSearch : Graphs("group_search_graph")
}

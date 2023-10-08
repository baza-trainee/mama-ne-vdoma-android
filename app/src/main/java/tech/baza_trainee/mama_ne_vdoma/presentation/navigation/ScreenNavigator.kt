package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.navigation.NavHostController
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CommonRoute

class ScreenNavigator private constructor(controller: NavHostController) {

    private var navHostController: NavHostController? = null

    init {
        navHostController = controller
    }

    fun navigate(route: CommonRoute) {
        navHostController?.navigate(route.destination)
    }

    fun goBack() {
        navHostController?.popBackStack()
    }

    fun getBackStackEntry(navGraphRoute: String) = navHostController?.getBackStackEntry(navGraphRoute)

    companion object {

        private var instance: ScreenNavigator? = null

        fun newInstance(controller: NavHostController) {
            if (instance == null)
                instance = ScreenNavigator(controller)
        }

        fun get() = instance
    }
}
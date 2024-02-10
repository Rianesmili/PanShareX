package fr.mastersime.pansharexadmin.setup

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.mastersime.pansharexadmin.setup.Screen.ADMIN_VIEW_ROUTE
import fr.mastersime.pansharexadmin.ui.feature.AdminView

@Composable
fun SetupNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = ADMIN_VIEW_ROUTE,
    ) {
        composable(
            route = ADMIN_VIEW_ROUTE,
        ) {
            AdminView()
        }
    }
}
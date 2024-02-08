package fr.mastersime.pansharex.setup

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.mastersime.pansharex.feature.home.HomeView
import fr.mastersime.pansharex.setup.Screen.HOME_VIEW_ROUTE

@Composable
fun SetupNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = HOME_VIEW_ROUTE,
    ) {
        composable(
            route = HOME_VIEW_ROUTE,
        ) {
            HomeView(navController)
        }
    }
}

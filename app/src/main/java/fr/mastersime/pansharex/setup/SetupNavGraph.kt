package fr.mastersime.pansharex.setup

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.mastersime.pansharex.data.Location
import fr.mastersime.pansharex.data.PhotoData
import fr.mastersime.pansharex.feature.home.HomeView
import fr.mastersime.pansharex.feature.summary.SummaryView
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
            HomeView(navController = navController)
        }
        composable(
            route = Screen.SUMMURY_VIEW_ROUTE,
        ) {
            val mockLocation = Location(longitude = 0.0, latitude = 0.0)
            SummaryView(PhotoData(image = null, location = mockLocation, type = "Unknown"))
        }
    }
}

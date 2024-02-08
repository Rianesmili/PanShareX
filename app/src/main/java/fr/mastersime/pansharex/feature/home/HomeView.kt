package fr.mastersime.pansharex.feature.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import fr.mastersime.pansharex.feature.grantpermission.NoPermissionScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeView(navController: NavController) {

    val cameraPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    val locationPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.ACCESS_FINE_LOCATION)

    when {

        cameraPermissionState.status.isGranted && locationPermissionState.status.isGranted -> {
            // TODO: Add the content of the home screen
        }

        cameraPermissionState.status.shouldShowRationale || locationPermissionState.status.shouldShowRationale -> {
            NoPermissionScreen(
                onRequestCameraPermission = { cameraPermissionState.launchPermissionRequest() },
                onRequestLocationPermission = { locationPermissionState.launchPermissionRequest() }
            )
        }

        else -> {
            NoPermissionScreen(
                onRequestCameraPermission = { cameraPermissionState.launchPermissionRequest() },
                onRequestLocationPermission = { locationPermissionState.launchPermissionRequest() }
            )
        }
    }

}
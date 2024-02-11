package fr.mastersime.pansharex.feature.grantpermission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun NoPermissionScreen(
    onRequestCameraPermission: () -> Unit,
    onRequestLocationPermission: () -> Unit,
    onRequestReadExternalStoragePermission: () -> Unit
) {
    NoPermissionContent(
        onRequestCameraPermission = onRequestCameraPermission,
        onRequestLocationPermission = onRequestLocationPermission,
        onRequestReadExternalStoragePermission = onRequestReadExternalStoragePermission
    )
}

@Composable
private fun NoPermissionContent(
    onRequestCameraPermission: () -> Unit,
    onRequestLocationPermission: () -> Unit,
    onRequestReadExternalStoragePermission: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Please grant the permissions to use the core functionality of this app.")
        Button(onClick = onRequestCameraPermission) {
            Icon(imageVector = Icons.Default.Phone, contentDescription = "Camera")
            Text(text = "Grant Camera permission")
        }
        Button(onClick = onRequestLocationPermission) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Location")
            Text(text = "Grant Location permission")
        }
        Button(onClick = onRequestReadExternalStoragePermission) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Gallery")
            Text(text = "Grant Gallery access permission")
        }
    }
}
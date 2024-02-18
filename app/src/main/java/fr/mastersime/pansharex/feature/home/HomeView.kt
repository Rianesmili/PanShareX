package fr.mastersime.pansharex.feature.home

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import fr.mastersime.pansharex.R
import fr.mastersime.pansharex.feature.grantpermission.NoPermissionScreen
import fr.mastersime.pansharex.setup.Screen.SUMMURY_VIEW_ROUTE
import fr.mastersime.pansharex.setup.classifyImage
import fr.mastersime.pansharex.setup.getBitmapFromUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeView(navController: NavController) {

    val homeViewModel: HomeViewModel = hiltViewModel()

    val cameraPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    val locationPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.ACCESS_FINE_LOCATION)

    val readExternalStoragePermissionState =
        rememberPermissionState(permission = android.Manifest.permission.READ_EXTERNAL_STORAGE)


    when {

        cameraPermissionState.status.isGranted && locationPermissionState.status.isGranted && readExternalStoragePermissionState.status.isGranted -> {
            CameraView(navController = navController, homeViewModel = homeViewModel)
        }

        cameraPermissionState.status.shouldShowRationale || locationPermissionState.status.shouldShowRationale || readExternalStoragePermissionState.status.shouldShowRationale -> {
            NoPermissionScreen(onRequestCameraPermission = { cameraPermissionState.launchPermissionRequest() },
                onRequestLocationPermission = { locationPermissionState.launchPermissionRequest() },
                onRequestReadExternalStoragePermission = { readExternalStoragePermissionState.launchPermissionRequest() }
            )
        }

        else -> {
            NoPermissionScreen(
                onRequestCameraPermission = { cameraPermissionState.launchPermissionRequest() },
                onRequestLocationPermission = { locationPermissionState.launchPermissionRequest() },
                onRequestReadExternalStoragePermission = { readExternalStoragePermissionState.launchPermissionRequest() }

            )
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraView(navController: NavController, homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var imageCapture = remember { mutableStateOf<ImageCapture?>(null) }

    var isProcessing = remember { mutableStateOf(false) } // Ajoutez cet état

    val outputDirectory = context.externalMediaDirs.firstOrNull()?.let {
        File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    when (isProcessing.value) {
        true -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Affichez un indicateur de progression si isProcessing est true
            }
        }

        false -> {
            Column(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val executor = ContextCompat.getMainExecutor(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            imageCapture.value = ImageCapture.Builder().build()
                            bindPreview(
                                cameraProvider,
                                previewView,
                                lifecycleOwner,
                                imageCapture.value
                            )
                        }, executor)
                        previewView
                    }, modifier = Modifier.fillMaxSize(0.8f)
                )
                Button(onClick = {
                    if (imageCapture.value != null) {
                        Log.d("CameraView", "Hello From onCLick Bouton")

                        isProcessing.value = true // Définissez isProcessing à true avant l'appel
                        CoroutineScope(Dispatchers.Default).launch {
                            val className = homeViewModel.takePictureAndGetClass(
                                imageCapture.value,
                                outputDirectory,
                                context
                            )
                            delay(2000)
                            isProcessing.value = false // Set isProcessing to false after the call
                            Log.d("CameraView", "Hello From className: $className")


                            withContext(Dispatchers.Main) {
                                navController.navigate(
                                    SUMMURY_VIEW_ROUTE.replace(
                                        "{className}",
                                        className
                                    )
                                )
                            }
                        }

                    } else {
                        Log.e("CameraView", "Camera initialization is not complete")
                    }
                }, modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                    content = {
                        Text("Détecter le panneau")
                    })
                GalleryButton(homeViewModel = homeViewModel, navController = navController, isProcessing = isProcessing)
            }
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GalleryButton(navController: NavController, homeViewModel: HomeViewModel, isProcessing: MutableState<Boolean>) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = getBitmapFromUri(context, it)
                bitmap?.let { bmp ->
                    CoroutineScope(Dispatchers.Default).launch {
                        isProcessing.value = true // Définissez isProcessing à true avant l'appel
                        val className = classifyImage(context = context, bitmap = bmp)
                        delay(2000)
                        isProcessing.value = false
                        Log.d("GalleryButton", "Hello From className: $className")

                        withContext(Dispatchers.Main) {
                            navController.navigate(
                                SUMMURY_VIEW_ROUTE.replace(
                                    "{className}",
                                    className
                                )
                            )
                        }
                    }
                }
            }
        }
    Button(
        onClick = {
            launcher.launch("image/*")
        },
        content = {
            Text("Choisir une image depuis la galerie")
        },
        modifier = Modifier
            .padding(16.dp)
    )
}


private fun bindPreview(
    cameraProvider: ProcessCameraProvider,
    previewView: PreviewView,
    lifecycleOwner: LifecycleOwner,
    imageCapture: ImageCapture?
) {
    val preview = Preview.Builder().build().also {
        it.setSurfaceProvider(previewView.surfaceProvider)
    }

    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    try {
        cameraProvider.unbindAll()
        if (imageCapture != null) {
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
        } else {
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
        }
    } catch (exc: Exception) {
        Log.e("CameraView", "Use case binding failed", exc)
    }
}

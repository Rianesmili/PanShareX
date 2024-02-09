package fr.mastersime.pansharex.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import fr.mastersime.pansharex.setup.addImageToGallery
import fr.mastersime.pansharex.setup.correctBitmapOrientation
import fr.mastersime.pansharex.setup.createPhotoFile
import fr.mastersime.pansharex.setup.runModelInference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RepositoryAppImpl @Inject constructor() : RepositoryApp {

    override suspend fun takePictureAndGetClass(
        imageCapture: ImageCapture?,
        outputDirectory: File?,
        context: Context
    ): String = suspendCoroutine { continuation ->
        var className = ""
        val photoFile = createPhotoFile(outputDirectory)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.let {
            CoroutineScope(Dispatchers.IO).launch {

                it.takePicture(outputOptions, ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            val savedUri = Uri.fromFile(photoFile)

                            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, savedUri)

                            val rotatedBitmap = correctBitmapOrientation(photoFile, bitmap)

                            className = runModelInference(context, rotatedBitmap)

                            Toast.makeText(context, className, Toast.LENGTH_SHORT).show()
                            Log.d("CameraContent", "Hello From class name: $className")

                            addImageToGallery(context, photoFile, rotatedBitmap)
                            // Resume the coroutine with the class name
                            continuation.resume(className)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e(
                                "CameraView",
                                "Photo capture failed: ${exception.message}",
                                exception
                            )
                        }
                    }
                )
            }

        }
    }
}
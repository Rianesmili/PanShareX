package fr.mastersime.pansharex.repository

import android.content.Context
import androidx.camera.core.ImageCapture
import java.io.File

interface RepositoryApp {
    suspend fun takePictureAndGetClass(imageCapture: ImageCapture?, outputDirectory: File?, context: Context) : String
}
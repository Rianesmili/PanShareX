package fr.mastersime.pansharex.repository

import android.content.Context
import androidx.camera.core.ImageCapture
import java.io.File

interface RepositoryApp {
    suspend fun takePicture(imageCapture: ImageCapture?, outputDirectory: File?, context: Context)
}
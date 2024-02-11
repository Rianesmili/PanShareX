package fr.mastersime.pansharex.repository

import android.content.Context
import androidx.camera.core.ImageCapture
import fr.mastersime.pansharex.data.PhotoData
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

interface RepositoryApp {
    suspend fun takePictureAndGetClass(imageCapture: ImageCapture?, outputDirectory: File?, context: Context) : String
    suspend fun sendPhotoData(photoData: PhotoData): Response<ResponseBody>
}
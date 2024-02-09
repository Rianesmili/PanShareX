package fr.mastersime.pansharex.feature.home

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersime.pansharex.repository.RepositoryApp
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RepositoryApp
) : ViewModel(){
    suspend fun takePicture(imageCapture: ImageCapture?, outputDirectory: File?, context: Context) {
        repository.takePicture(imageCapture, outputDirectory, context)
    }
}
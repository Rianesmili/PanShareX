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
    suspend fun takePictureAndGetClass(imageCapture: ImageCapture?, outputDirectory: File?, context: Context) : String{
       return repository.takePictureAndGetClass(imageCapture, outputDirectory, context)
    }
}
package fr.mastersime.pansharex.feature.home

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersime.pansharex.data.Location
import fr.mastersime.pansharex.data.PhotoData
import fr.mastersime.pansharex.repository.LocalisationRepository
import fr.mastersime.pansharex.repository.RepositoryApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RepositoryApp,
    private val locationRepository: LocalisationRepository
) : ViewModel(){

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> get() = _location

    private val _photoData = MutableStateFlow<PhotoData?>(null)
    val photoData: StateFlow<PhotoData?> = _photoData

    init {
        fetchLocation()
    }

    fun fetchLocation() {
        viewModelScope.launch {
            val androidLocation = locationRepository.getLastKnownLocation()
            _location.value = androidLocation?.let {
                Location(it.latitude, it.longitude)
            }
        }
    }

    fun updatePhotoData(photoData: PhotoData) {
        _photoData.value = photoData
    }

    fun sendPhotoDataToBackend() {
        val gson = Gson()
        val json = gson.toJson(_photoData.value)
        // Simuler l'envoi du JSON au backend
        Log.d("", "Hello from Envoi du JSON au backend: $json")
    }

    suspend fun takePictureAndGetClass(imageCapture: ImageCapture?, outputDirectory: File?, context: Context) : String{
       return repository.takePictureAndGetClass(imageCapture, outputDirectory, context)
    }
}
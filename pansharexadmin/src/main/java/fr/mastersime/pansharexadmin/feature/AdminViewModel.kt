package fr.mastersime.pansharexadmin.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersime.pansharexadmin.data.Location
import fr.mastersime.pansharexadmin.data.PhotoData
import fr.mastersime.pansharexadmin.webservice.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val _photoData = MutableStateFlow<List<PhotoData>?>(null)
    val photoData: StateFlow<List<PhotoData>?> = _photoData

    init {
        fetchPhotos()
    }

    private fun fetchPhotos() {
        viewModelScope.launch {
            val response = apiService.getPhotos()
            _photoData.value = response.map { photoResponse ->
                PhotoData(
                    location = Location(
                        latitude = photoResponse.location.latitude,
                        longitude = photoResponse.location.longitude
                    ),
                    type = photoResponse.type
                )
            }
        }
    }
}
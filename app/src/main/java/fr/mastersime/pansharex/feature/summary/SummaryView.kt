package fr.mastersime.pansharex.feature.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.mastersime.pansharex.data.Location
import fr.mastersime.pansharex.data.PhotoData
import fr.mastersime.pansharex.feature.home.HomeViewModel

@Composable
fun SummaryView(className: String? = "Unknown") {

    val homeviewModel: HomeViewModel = hiltViewModel()

    var locationModel by remember { mutableStateOf<Location?>(null) }
    var photoData by remember { mutableStateOf<PhotoData?>(null) }


    LaunchedEffect(homeviewModel) {
        homeviewModel.location.collect { location ->
            locationModel = location
            photoData = PhotoData(location = location, type = className)

            homeviewModel.updatePhotoData(photoData ?: PhotoData(null, null, "Unknown"))
            homeviewModel.sendPhotoDataToBackend()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Coordonées GPS :",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 25.sp,
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "Latitude : ${photoData?.location?.latitude}",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 20.sp,

            )
        Text(
            text = "Longitude:  ${photoData?.location?.longitude}",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "Type de Panneau : ",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 25.sp,
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = photoData?.type ?: "Unretrived",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 20.sp,
        )
    }
}



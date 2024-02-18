package fr.mastersime.pansharexadmin.feature

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.mastersime.pansharexadmin.data.PhotoData

@Composable
fun AdminView(context: Context) {

    val adminViewModel: AdminViewModel = hiltViewModel()
    val photoData by adminViewModel.photoData.collectAsState()

    Column {
        HomeHeader()
        if (photoData == null) {
            CircularProgressIndicator()
        } else {
            val sortedPhotoData = photoData!!.sortedByDescending { it.date }
            LazyColumn {
                items(sortedPhotoData) {
                    PhotoDataRow(it, context = context)
                }
            }
        }
    }
}

@Composable
fun PhotoDataRow(photoData: PhotoData?, context: Context) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(18.dp)
            .border(width = Dp(1f), color = Color.Gray, shape = RoundedCornerShape(4.dp)) // Add this line
            .clickable {
                photoData?.location?.let { location ->
                    val intentUri =
                        Uri.parse("geo:${location.longitude},${location.latitude}?q=${location.longitude},${location.latitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                }
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            Modifier.weight(1f)
        ) {
            Text(
                text = "Date : ${photoData?.date.toString()}",
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = "Categorie de panneau : ${photoData?.type}" ?: "Panneau de Danger",
                modifier = Modifier
                    .padding(8.dp)
            )

            Text(
                text = "Latitude : ${photoData?.location?.latitude.toString()}",
                modifier = Modifier
                    .padding(8.dp)
            )

            Text(
                text = "Longitude : ${photoData?.location?.longitude.toString()}",
                modifier = Modifier
                    .padding(8.dp)

            )
        }
    }
}

@Composable
fun HomeHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "PANSHAREX ADMIN",
            fontSize = 20.sp,
            color = Color.White,
            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

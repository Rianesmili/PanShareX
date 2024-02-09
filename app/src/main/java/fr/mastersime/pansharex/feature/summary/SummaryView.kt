package fr.mastersime.pansharex.feature.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mastersime.pansharex.data.PhotoData

@Composable
fun SummaryView(photoData: PhotoData?) {
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
            text = photoData?.type ?: "Unknown",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 20.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryViewPreview() {
    SummaryView(
        PhotoData(
            image = null,
            location = null,
            type = "Unknown"
        )
    )
}



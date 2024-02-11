package fr.mastersime.pansharexadmin.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mastersime.pansharexadmin.data.Location
import fr.mastersime.pansharexadmin.data.PhotoData

@Composable
fun AdminView() {
    Column {
        HomeHeader()
        ListeOfPhotoData()
    }
}

@Composable
fun ListeOfPhotoData() {
    LazyColumn() {
        item {
            PhotoDataRow(
                PhotoData(
                    type = "Panneau de danger",
                    location = Location(1.452657, 5.51657687)
                )
            )
        }
    }
}

@Composable
fun PhotoDataRow(photoData: PhotoData?) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column (
            Modifier.weight(1f)
        ) {
            Text(
                text = "Latitude : ${photoData?.location?.latitude.toString()}",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
            )

            Text(
                text = "Longitude : ${photoData?.location?.longitude.toString()}",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
            )
        }

        Text(
            text = photoData?.type ?: "Panneau de Danger",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Composable
fun HomeHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
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

@Preview(showBackground = true)
@Composable
fun HomeHeaderPreview() {
    HomeHeader()
}

@Preview(showBackground = true)
@Composable
fun PhotoDataRowPreview() {
    ListeOfPhotoData()
}

package fr.mastersime.pansharexadmin.data

import android.graphics.Bitmap

data class PhotoData(
    val image: Bitmap? = null,
    val location: Location? = null,
    var type: String? = "Unknown"
)

data class Location(
    val latitude: Double?,
    val longitude: Double?
)
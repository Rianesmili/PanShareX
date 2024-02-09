package fr.mastersime.pansharex.data

import android.graphics.Bitmap

data class PhotoData(
    val image: Bitmap? = null,
    val location: Location? = null,
    var type: String? = "Unknown"
)

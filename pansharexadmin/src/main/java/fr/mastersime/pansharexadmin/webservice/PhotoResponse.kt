package fr.mastersime.pansharexadmin.webservice

import fr.mastersime.pansharexadmin.data.Location

data class PhotoResponse(
    val id: Int,
    val image: String?,
    val location: Location,
    val type: String
)

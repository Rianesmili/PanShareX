package fr.mastersime.pansharexadmin.webservice

import retrofit2.http.GET

interface ApiService {
    @GET("api/photos")
    suspend fun getPhotos(): List<PhotoResponse>
}
package fr.mastersime.pansharex.webservices

import fr.mastersime.pansharex.data.PhotoData
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface PhotoApi {
    @POST("api/photos")
    suspend fun sendPhotoData(@Body photoData: PhotoData): Response<ResponseBody>
}
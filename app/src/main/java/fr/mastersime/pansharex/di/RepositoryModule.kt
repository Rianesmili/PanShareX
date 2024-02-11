package fr.mastersime.pansharex.di

import android.content.Context
import android.provider.ContactsContract.Contacts.Photo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.mastersime.pansharex.repository.RepositoryApp
import fr.mastersime.pansharex.repository.RepositoryAppImpl
import fr.mastersime.pansharex.webservices.PhotoApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule{

    @Provides
    fun provideRepositoryApp(photoApi : PhotoApi): RepositoryApp = RepositoryAppImpl(photoApi)

    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun providePhotoApi(retrofit: Retrofit): PhotoApi {
        return retrofit.create(PhotoApi::class.java)
    }

}
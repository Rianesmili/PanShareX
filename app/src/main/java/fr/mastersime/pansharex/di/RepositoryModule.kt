package fr.mastersime.pansharex.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.mastersime.pansharex.repository.RepositoryApp
import fr.mastersime.pansharex.repository.RepositoryAppImpl

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule{

    @Provides
    fun provideRepositoryApp(): RepositoryApp = RepositoryAppImpl()

    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

}
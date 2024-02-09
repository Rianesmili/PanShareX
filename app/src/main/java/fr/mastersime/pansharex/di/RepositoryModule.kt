package fr.mastersime.pansharex.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.mastersime.pansharex.repository.RepositoryApp
import fr.mastersime.pansharex.repository.RepositoryAppImpl

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule{

    @Provides
    fun provideRepositoryApp(): RepositoryApp = RepositoryAppImpl()

}
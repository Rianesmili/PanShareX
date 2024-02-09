package fr.mastersime.pansharex.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule{

    @Provides
    fun provideRepositoryApp(): RepositoryApp = RepositoryAppImpl()

}
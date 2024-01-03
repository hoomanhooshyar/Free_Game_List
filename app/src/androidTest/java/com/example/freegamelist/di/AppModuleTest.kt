package com.example.freegamelist.di

import android.app.Application
import androidx.room.Room
import com.example.freegamelist.data.local.GameDatabase
import com.example.freegamelist.data.repository.FakeGameRepository
import com.example.freegamelist.domain.repository.GameRepository
import com.example.freegamelist.domain.use_case.GetAllGamesFromLocalUseCase
import com.example.freegamelist.domain.use_case.GetAllGamesUseCase
import com.example.freegamelist.domain.use_case.GetGameByIdUseCase
import com.example.freegamelist.domain.use_case.GetGamesByGenreUseCase
import com.example.freegamelist.presentation.util.internet_connection.FakeNetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.junit.Assert.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModuleTest{

    @Provides
    @Singleton
    fun provideGetAllGamesUseCase(repository: FakeGameRepository): GetAllGamesUseCase {
        return GetAllGamesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetGameByIdUseCase(repository: FakeGameRepository): GetGameByIdUseCase {
        return GetGameByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetGamesByGenreUseCase(repository: FakeGameRepository): GetGamesByGenreUseCase {
        return GetGamesByGenreUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllGamesFromLocalUseCase(repository: FakeGameRepository): GetAllGamesFromLocalUseCase {
        return GetAllGamesFromLocalUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGameRepository(
        connectivityObserver: FakeNetworkConnectivityObserver
    ):GameRepository{
        return FakeGameRepository(connectivityObserver)
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(): FakeNetworkConnectivityObserver {
        return FakeNetworkConnectivityObserver()
    }

    @Provides
    @Singleton
    fun provideGameDatabase(app: Application): GameDatabase {
        return Room
            .inMemoryDatabaseBuilder(
                app,
                GameDatabase::class.java
            )
            .build()
    }
}
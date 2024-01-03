package com.example.freegamelist.di

import android.app.Application
import androidx.room.Room
import com.example.freegamelist.data.local.GameDatabase
import com.example.freegamelist.data.remote.GameApi
import com.example.freegamelist.data.repository.GameRepositoryImpl
import com.example.freegamelist.domain.repository.GameRepository
import com.example.freegamelist.domain.use_case.GetAllGamesFromLocalUseCase
import com.example.freegamelist.domain.use_case.GetAllGamesUseCase
import com.example.freegamelist.domain.use_case.GetGameByIdUseCase
import com.example.freegamelist.domain.use_case.GetGamesByGenreUseCase
import com.example.freegamelist.presentation.util.internet_connection.NetworkConnectivityObserver
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGetAllGamesUseCase(repository: GameRepository):GetAllGamesUseCase{
        return GetAllGamesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetGameByIdUseCase(repository: GameRepository):GetGameByIdUseCase{
        return GetGameByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetGamesByGenreUseCase(repository: GameRepository):GetGamesByGenreUseCase{
        return GetGamesByGenreUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllGamesFromLocalUseCase(repository: GameRepository):GetAllGamesFromLocalUseCase{
        return GetAllGamesFromLocalUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGameRepository(
        db:GameDatabase,
        api:GameApi
    ):GameRepository{
        return GameRepositoryImpl(api,db.dao)
    }

    @Provides
    @Singleton
    fun provideGameDatabase(app:Application):GameDatabase{
        return Room
            .databaseBuilder(app,GameDatabase::class.java,"game_db")
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi():Moshi{
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun provideGameApi(moshi: Moshi):GameApi{
        return Retrofit.Builder()
            .baseUrl(GameApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GameApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(app:Application):NetworkConnectivityObserver{
        return NetworkConnectivityObserver(app)
    }
}
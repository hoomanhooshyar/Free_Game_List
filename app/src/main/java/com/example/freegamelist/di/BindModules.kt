package com.example.freegamelist.di

import com.example.freegamelist.presentation.util.internet_connection.ConnectivityObserver
import com.example.freegamelist.presentation.util.internet_connection.NetworkConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModules {
    @Binds
    @Singleton
    abstract fun bindNetworkConnectivityObserver(
        networkConnectivityObserver: NetworkConnectivityObserver
    ):ConnectivityObserver
}
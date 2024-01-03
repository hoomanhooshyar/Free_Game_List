package com.example.freegamelist.presentation.util.internet_connection

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe():Flow<Status>
}
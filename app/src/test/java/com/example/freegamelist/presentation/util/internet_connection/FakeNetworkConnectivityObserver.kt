package com.example.freegamelist.presentation.util.internet_connection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class FakeNetworkConnectivityObserver:ConnectivityObserver{
    private val statusFlow = MutableStateFlow(Status.Unavailable)

    fun setStatus(status: Status){
        statusFlow.value = status
    }
    override fun observe(): Flow<Status> {
        return statusFlow
    }

}
package com.example.freegamelist.presentation.game_details

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freegamelist.core.util.Resource
import com.example.freegamelist.domain.use_case.GetGameByIdUseCase
import com.example.freegamelist.presentation.util.UIEvent
import com.example.freegamelist.presentation.util.internet_connection.ConnectivityObserver
import com.example.freegamelist.presentation.util.internet_connection.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val getGameByIdUseCase: GetGameByIdUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private var _state = mutableStateOf(DetailsState())
    var state: State<DetailsState> = _state

    private val _status = MutableStateFlow(Status.Unavailable)
    val status: StateFlow<Status> get() = _status

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        networkConnectionCheck()
    }

    fun showSnackBar(message:String){
        viewModelScope.launch {
            _eventFlow.emit(
                UIEvent.ShowSnackBar(
                    message
                )
            )
        }
    }

    private fun networkConnectionCheck() {
        viewModelScope.launch {
            connectivityObserver.observe().collect{ newStatus ->
                _status.value = newStatus
            }
        }
    }

    fun getGameById(id: Int) {
        viewModelScope.launch {
            getGameByIdUseCase(id).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            game = result.data,
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        Log.e("Error", result.message!!)
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            game = null,
                            isLoading = true
                        )
                    }
                }
            }.launchIn(this)
        }
    }


}
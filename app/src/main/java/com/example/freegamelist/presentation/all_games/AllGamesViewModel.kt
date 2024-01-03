package com.example.freegamelist.presentation.all_games

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.freegamelist.domain.use_case.GetAllGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.freegamelist.core.util.Resource
import com.example.freegamelist.domain.model.Game
import com.example.freegamelist.domain.use_case.GetAllGamesFromLocalUseCase
import com.example.freegamelist.domain.use_case.GetGamesByGenreUseCase
import com.example.freegamelist.presentation.util.UIEvent
import com.example.freegamelist.presentation.util.internet_connection.ConnectivityObserver
import com.example.freegamelist.presentation.util.internet_connection.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@HiltViewModel
class AllGamesViewModel @Inject constructor(
    private val getGame: GetAllGamesUseCase,
    private val getGamesByGenre: GetGamesByGenreUseCase,
    private val getAllGamesFromLocalUseCase: GetAllGamesFromLocalUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private val _state = mutableStateOf(AllGamesState())
    val state: State<AllGamesState> = _state



    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _status = MutableStateFlow(Status.Unavailable)
    val status: StateFlow<Status> get() = _status

    init {
        networkConnectionCheck()
    }

    private fun networkConnectionCheck() {
        viewModelScope.launch {
            connectivityObserver.observe().collect{ newStatus ->
                _status.value = newStatus
                if(_status.value == Status.Available){
                    getAllGames()
                }else{
                    getAllGamesFromLocal()
                }
            }
        }
    }

    fun getAllGames() {
        viewModelScope.launch {
            getGame().collect { result ->
                withContext(Dispatchers.Main){
                    handleResult(result)
                }
            }
        }
    }

    fun getAllGamesFromLocal(){
        viewModelScope.launch {
            getAllGamesFromLocalUseCase().collect{ result ->
                withContext(Dispatchers.Main){
                    handleResult(result)
                }
            }
        }
    }

    fun getTaggedGames(genre: String) {

        viewModelScope.launch {
            getGamesByGenre(genre).collect { result ->
                withContext(Dispatchers.Main){
                    handleResult(result)
                }
            }
        }
    }

    private fun handleResult(result:Resource<List<Game>>){
        when (result) {
            is Resource.Success ->{
                _state.value = _state.value.copy(
                    allGames = result.data ?: emptyList(),
                    isLoading = false
                )
            }
            is Resource.Error ->{
                _state.value = _state.value.copy(
                    allGames = result.data ?: emptyList(),
                    isLoading = false,
                )
                viewModelScope.launch {
                    _eventFlow.emit(
                        UIEvent.ShowSnackBar(
                            result.message!!
                        )
                    )
                }

            }
            is Resource.Loading ->{
                _state.value = _state.value.copy(
                    allGames = result.data ?: emptyList(),
                    isLoading = true,
                )
            }
        }
    }
}
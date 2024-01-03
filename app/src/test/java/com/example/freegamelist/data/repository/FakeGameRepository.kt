package com.example.freegamelist.data.repository

import com.example.freegamelist.core.util.Resource
import com.example.freegamelist.domain.model.Game
import com.example.freegamelist.domain.repository.GameRepository
import com.example.freegamelist.presentation.util.internet_connection.FakeNetworkConnectivityObserver
import com.example.freegamelist.presentation.util.internet_connection.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeGameRepository @Inject constructor(
    private val connectivityObserver: FakeNetworkConnectivityObserver
):GameRepository{
    private var games = mutableListOf<Game>()

    override fun getAllGames(): Flow<Resource<List<Game>>> {

        return flow {
            emit(when(connectivityObserver.observe().first()){
                Status.Available ->{
                    Resource.Success(games)
                }
                else ->{
                    Resource.Error("No Internet Connection")
                }
            })
        }
    }
    override fun getAllGamesFromLocal(): Flow<Resource<List<Game>>> {
        return flow {
            emit(when(connectivityObserver.observe().first()){
                Status.Available ->{
                    Resource.Error(message = "Use getAllGames Function")
                }
                else ->{
                    Resource.Success(games)
                }
            })
        }
    }
    override fun getGameById(id: Int): Flow<Resource<Game>> {
        val game = games.find { it.id == id }
        return flow { emit(Resource.Success(game)) }
    }

    override fun getGameByGenre(genre: String): Flow<Resource<List<Game>>> {
        val taggedGames = mutableListOf<Game>()
        games.forEach { game ->
            if(game.genre == genre){
                taggedGames.add(game)
            }
        }
        return flow { emit(Resource.Success(taggedGames)) }
    }



    fun insertGame(insertedGames:List<Game>){
        games.addAll(insertedGames)
    }

}
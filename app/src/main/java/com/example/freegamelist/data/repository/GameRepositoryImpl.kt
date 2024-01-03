package com.example.freegamelist.data.repository

import android.util.Log
import com.example.freegamelist.core.util.Resource
import com.example.freegamelist.data.local.GameDao
import com.example.freegamelist.data.remote.GameApi
import com.example.freegamelist.domain.model.Game
import com.example.freegamelist.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GameRepositoryImpl(
    private val api: GameApi,
    private val dao: GameDao
) : GameRepository {
    override fun getAllGames(): Flow<Resource<List<Game>>> = flow {
        emit(Resource.Loading())
        val games = dao.getAllGames().map { it.toGame() }
        emit(Resource.Loading(data = games))
        try {
            val remoteGame = api.getAllGames()
            dao.deleteGames(remoteGame.map { it.title })
            dao.insertGames(remoteGame.map { it.toGameEntity() })
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.message!!, data = games))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.message!!, data = games))
        }

        val newGames = dao.getAllGames().map { it.toGame() }
        emit(Resource.Success(data = newGames))
    }

    override fun getGameById(id: Int): Flow<Resource<Game>> = flow {
        emit(Resource.Loading())
        try {
            val game = dao.getGameById(id).toGame()
            Log.d("GameRepository", "Loaded game by id $id: $game")
            emit(Resource.Success(data = game))
        } catch (e: Exception) {
            Log.e("GameRepository", "Error loading game by id $id: ${e.message}")
            emit(Resource.Error(message = e.message!!, data = null))
        }


    }

    override fun getGameByGenre(genre: String): Flow<Resource<List<Game>>> = flow {
        emit(Resource.Loading())
        val games = dao.getGameByGenre(genre).map { it.toGame() }
        emit(Resource.Success(data = games))
    }

    override fun getAllGamesFromLocal(): Flow<Resource<List<Game>>> = flow {
        emit(Resource.Loading())
        val games = dao.getAllGames().map { it.toGame() }
        emit(Resource.Success(data = games))
    }
}
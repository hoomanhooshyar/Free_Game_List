package com.example.freegamelist.domain.repository

import com.example.freegamelist.core.util.Resource
import com.example.freegamelist.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getAllGames():Flow<Resource<List<Game>>>
    fun getGameById(id:Int):Flow<Resource<Game>>
    fun getGameByGenre(genre:String):Flow<Resource<List<Game>>>
    fun getAllGamesFromLocal():Flow<Resource<List<Game>>>
}
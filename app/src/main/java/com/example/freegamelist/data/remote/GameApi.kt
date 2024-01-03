package com.example.freegamelist.data.remote

import com.example.freegamelist.data.remote.dto.GameDto
import retrofit2.http.GET

interface GameApi {

    @GET("games")
    suspend fun getAllGames():List<GameDto>


    companion object{
        const val BASE_URL = "https://www.freetogame.com/api/"
    }
}
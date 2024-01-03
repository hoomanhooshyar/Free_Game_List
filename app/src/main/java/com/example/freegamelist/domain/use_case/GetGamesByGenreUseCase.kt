package com.example.freegamelist.domain.use_case

import com.example.freegamelist.core.util.Resource
import com.example.freegamelist.domain.model.Game
import com.example.freegamelist.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow

class GetGamesByGenreUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(genre:String):Flow<Resource<List<Game>>>{

        return repository.getGameByGenre(genre)
    }
}
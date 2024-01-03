package com.example.freegamelist.presentation.all_games

import com.example.freegamelist.domain.model.Game

data class AllGamesState(
    val allGames:List<Game>? = emptyList(),
    val isLoading:Boolean = false,
)

package com.example.freegamelist.presentation.game_details

import com.example.freegamelist.domain.model.Game

data class DetailsState(
    val game:Game? = null,
    val isLoading:Boolean = false
)

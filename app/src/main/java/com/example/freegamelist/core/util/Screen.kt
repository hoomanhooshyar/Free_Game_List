package com.example.freegamelist.core.util

sealed class Screen(val route:String){
    object AllGamesScreen:Screen("all_games_screen")
    object GameDetailsScreen:Screen("game_details_screen")
}

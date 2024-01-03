package com.example.freegamelist.presentation.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.freegamelist.core.util.Screen
import com.example.freegamelist.presentation.all_games.AllGamesScreen
import com.example.freegamelist.presentation.game_details.GameDetailsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.AllGamesScreen.route
    ){
        composable(Screen.AllGamesScreen.route){
            AllGamesScreen(navController = navController)
        }
        composable(
            route = "${Screen.GameDetailsScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.IntType
                }
            )
        ){
            GameDetailsScreen(id = it.arguments?.getInt("id"))
        }
    }

}
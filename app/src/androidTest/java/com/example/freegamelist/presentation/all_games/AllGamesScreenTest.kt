package com.example.freegamelist.presentation.all_games

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freegamelist.MainActivity
import com.example.freegamelist.core.util.Screen
import com.example.freegamelist.data.repository.FakeGameRepository
import com.example.freegamelist.di.AppModule
import com.example.freegamelist.domain.model.Game
import com.example.freegamelist.domain.use_case.GetAllGamesFromLocalUseCase
import com.example.freegamelist.domain.use_case.GetAllGamesUseCase
import com.example.freegamelist.domain.use_case.GetGamesByGenreUseCase
import com.example.freegamelist.presentation.util.internet_connection.FakeNetworkConnectivityObserver
import com.example.freegamelist.ui.theme.FreeGameListTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class)
class AllGamesScreenTest{
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var getAllGames:GetAllGamesUseCase

    @Inject
    lateinit var getAllGamesFromDB:GetAllGamesFromLocalUseCase


    @Inject
    lateinit var getGameByGenre:GetGamesByGenreUseCase

    @Inject
    lateinit var connectivityObserver: FakeNetworkConnectivityObserver

    private lateinit var repository: FakeGameRepository

    private lateinit var viewModel:AllGamesViewModel

    private lateinit var game:Game
    private val games = mutableListOf<Game>()

    @Before
    fun setup(){
        hiltRule.inject()
        repository = FakeGameRepository(connectivityObserver)
        viewModel = AllGamesViewModel(
            getGame = getAllGames,
            getGamesByGenre = getGameByGenre,
            getAllGamesFromLocalUseCase = getAllGamesFromDB,
            connectivityObserver = connectivityObserver
        )
        for(i in 0..10){
            game = Game(
                developer = "developer $i",
                freeToGameProfileUrl = "freeUrl $i",
                gameUrl = "gameUrl $i",
                genre = "genre $i",
                id = i,
                platform = "plat $i",
                publisher = "publish $i",
                releaseDate = "release $i",
                shortDescription = "short $i",
                thumbnail = "thumb $i",
                title = "title $i"
            )
            games.add(game)
        }
        repository.insertGame(games)

        composeRule.activity.setContent {
            val navController = rememberNavController()
            FreeGameListTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.AllGamesScreen.route
                ){
                    composable(route = Screen.AllGamesScreen.route){
                        AllGamesScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun toggleTagSection_isVisible(){
        composeRule.onNodeWithTag("tagRow").assertDoesNotExist()
        composeRule.onNodeWithTag("expandTag").performClick()
        composeRule.onNodeWithTag("tagRow").assertIsDisplayed()
    }
}
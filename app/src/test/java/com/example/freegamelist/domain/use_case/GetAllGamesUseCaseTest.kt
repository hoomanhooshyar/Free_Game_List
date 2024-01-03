package com.example.freegamelist.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.freegamelist.data.repository.FakeGameRepository
import com.example.freegamelist.domain.model.Game
import com.example.freegamelist.presentation.util.internet_connection.FakeNetworkConnectivityObserver
import com.example.freegamelist.presentation.util.internet_connection.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test



@ExperimentalCoroutinesApi
class GetAllGamesUseCaseTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var game:Game
    private lateinit var repository: FakeGameRepository
    private lateinit var connectivityObserver: FakeNetworkConnectivityObserver


    @Before
    fun setup(){
        connectivityObserver = FakeNetworkConnectivityObserver()
        repository = FakeGameRepository(connectivityObserver)
        val games = mutableListOf<Game>()
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

    }


    @Test
    fun `Get all games`() = runTest {
            connectivityObserver.setStatus(Status.Available)

            val result = repository.getAllGames()

            result.collect{
                assertThat(it.data).contains(game)
                
            }

    }
}
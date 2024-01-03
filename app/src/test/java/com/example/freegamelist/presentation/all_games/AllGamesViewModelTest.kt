package com.example.freegamelist.presentation.all_games

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.freegamelist.MainCoroutineRule
import com.example.freegamelist.data.repository.FakeGameRepository
import com.example.freegamelist.di.AppModule
import com.example.freegamelist.domain.model.Game
import com.example.freegamelist.domain.use_case.GetAllGamesFromLocalUseCase
import com.example.freegamelist.domain.use_case.GetAllGamesUseCase
import com.example.freegamelist.domain.use_case.GetGamesByGenreUseCase
import com.example.freegamelist.presentation.util.internet_connection.FakeNetworkConnectivityObserver
import com.example.freegamelist.presentation.util.internet_connection.Status
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
@UninstallModules(AppModule::class)
class AllGamesViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var connectivityObserver: FakeNetworkConnectivityObserver
    private lateinit var viewModel: AllGamesViewModel
    private lateinit var getAllGamesUseCase: GetAllGamesUseCase
    private lateinit var getAllGamesFromLocalUseCase: GetAllGamesFromLocalUseCase
    private lateinit var getGamesByGenreUseCase: GetGamesByGenreUseCase
    private lateinit var repository: FakeGameRepository
    private lateinit var game: Game
    private val games = mutableListOf<Game>()
    @Before
    fun setup(){
        connectivityObserver = FakeNetworkConnectivityObserver()

        repository = FakeGameRepository(connectivityObserver)
        getAllGamesUseCase = GetAllGamesUseCase(repository)
        getAllGamesFromLocalUseCase = GetAllGamesFromLocalUseCase(repository)
        getGamesByGenreUseCase = GetGamesByGenreUseCase(repository)
        connectivityObserver.setStatus(Status.Available)
        viewModel = AllGamesViewModel(
            getAllGamesUseCase,
            getGamesByGenreUseCase,
            getAllGamesFromLocalUseCase,
            connectivityObserver
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
    }

    @Test
    fun `Test getAllGames when Network is Available`() = runTest {
        connectivityObserver.setStatus(Status.Available)
        delay(500L)
        viewModel.getAllGames()

        assertThat(viewModel.status.value).isEqualTo(Status.Available)
        assertThat(viewModel.state.value.allGames).containsExactlyElementsIn(games)

    }

    @Test
    fun `Test getAllGamesFromLocal when Network is Unavailable`() = runTest {
        connectivityObserver.setStatus(Status.Unavailable)
        viewModel.getAllGamesFromLocal()
        delay(500L)
        assertThat(viewModel.status.value).isEqualTo(Status.Unavailable)
        assertThat(viewModel.state.value.allGames).containsExactlyElementsIn(games)
    }
}
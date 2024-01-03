

package com.example.freegamelist.presentation.all_games

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.freegamelist.R
import com.example.freegamelist.core.util.Screen
import com.example.freegamelist.presentation.all_games.components.GameItem
import com.example.freegamelist.presentation.all_games.components.GameTag
import com.example.freegamelist.presentation.util.UIEvent
import com.example.freegamelist.presentation.util.internet_connection.Status
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AllGamesScreen(
    viewModel: AllGamesViewModel = hiltViewModel(),
    navController: NavController
) {

    val snackBarHostState = remember{SnackbarHostState()}
    val status = viewModel.status.collectAsState()
    var isUnAvailable by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is UIEvent.ShowSnackBar ->{
                    snackBarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    LaunchedEffect(key1 = isUnAvailable){
        viewModel.getAllGamesFromLocal()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
        ) {
            when(status.value){
                Status.Available ->{
                    ShowGames(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
                Status.Losing ->{
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = stringResource(id = R.string.internet_losing),
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                        }
                        ShowGames(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
                Status.Lost ->{
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = stringResource(id = R.string.internet_lost),
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                        }
                        ShowGames(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
                Status.Unavailable ->{
                    isUnAvailable = true
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = stringResource(id = R.string.internet_unavailable),
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                        }
                        ShowGames(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShowGames(
    viewModel: AllGamesViewModel,
    navController: NavController
) {

    var expanded by remember{ mutableStateOf(false) }
    var isSelected by remember{ mutableStateOf(false) }
    var selectedIndex by remember{ mutableIntStateOf(0) }
    val state = viewModel.state.value

    val tags = listOf(
        "All",
        "mmorpg",
        "shooter",
        "strategy",
        "moba",
        "racing",
        "sports",
        "social",
        "sandbox",
        "open-world",
        "survival",
        "pvp",
        "pve",
        "pixel",
        "voxel",
        "zombie",
        "turn-based",
        "first-person",
        "third-Person",
        "top-down",
        "tank",
        "space",
        "sailing",
        "side-scroller",
        "superhero",
        "permadeath",
        "card",
        "battle-royale",
        "mmo",
        "mmofps",
        "mmotps",
        "3d",
        "2d",
        "anime",
        "fantasy",
        "sci-fi",
        "fighting",
        "action-rpg",
        "action",
        "military",
        "martial-arts",
        "flight",
        "low-spec",
        "tower-defense",
        "horror",
        "mmorts"
    )
    if(state.isLoading){
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }else {
        val games = state.allGames ?: emptyList()
        if (games.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(id = R.string.no_game),
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }

                Text(
                    text = stringResource(id = R.string.tags),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                AnimatedVisibility(visible = expanded) {
                    FlowRow(modifier = Modifier
                        .padding(vertical = 2.dp)
                        .testTag("tagRow")
                    ) {
                        for ((index, tag) in tags.withIndex()) {
                            isSelected = index == selectedIndex
                            Box(modifier = Modifier.padding(2.dp)) {
                                GameTag(
                                    tag = tag,
                                    isSelected = isSelected
                                ) {
                                    selectedIndex = index
                                    if (selectedIndex == 0) {
                                        if(viewModel.status.value == Status.Available){
                                            viewModel.getAllGames()
                                        }else{
                                            viewModel.getAllGamesFromLocal()
                                        }
                                    } else {
                                        viewModel.getTaggedGames(tag)
                                    }

                                }
                            }

                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            expanded = !expanded
                        }
                        .testTag("expandTag")
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(id = R.string.tags),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            ) {
                item {
                    Text(
                        text = stringResource(id = R.string.tags),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AnimatedVisibility(
                        visible = expanded
                    ) {
                        FlowRow(modifier = Modifier
                            .padding(vertical = 2.dp)
                            .testTag("tagRow")
                        ) {
                            for ((index, tag) in tags.withIndex()) {
                                isSelected = index == selectedIndex
                                Box(modifier = Modifier.padding(2.dp)) {
                                    GameTag(
                                        tag = tag,
                                        isSelected = isSelected
                                    ) {
                                        selectedIndex = index
                                        if (selectedIndex == 0) {
                                            if(viewModel.status.value == Status.Available){
                                                viewModel.getAllGames()
                                            }else{
                                                viewModel.getAllGamesFromLocal()
                                            }

                                        } else {
                                            viewModel.getTaggedGames(tag)
                                        }

                                    }
                                }

                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                expanded = !expanded
                            }
                            .testTag("expandTag")
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(id = R.string.tags),
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }

                }
                items(items = games) { game ->
                    GameItem(
                        game = game,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        navController.navigate(
                            "${Screen.GameDetailsScreen.route}/${game.id}"
                        )
                    }
                }
            }
        }
    }
}
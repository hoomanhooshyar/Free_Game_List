package com.example.freegamelist.presentation.game_details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.freegamelist.R
import com.example.freegamelist.presentation.util.UIEvent
import com.example.freegamelist.presentation.util.internet_connection.Status
import com.example.freegamelist.presentation.util.toFormattedDate
import com.example.freegamelist.ui.theme.ButtonColor
import kotlinx.coroutines.flow.collectLatest


@Composable
fun GameDetailsScreen(
    viewModel: GameDetailsViewModel = hiltViewModel(),
    id: Int?
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val state = viewModel.state.value
    val noConnection = stringResource(id = R.string.no_connection)
    LaunchedEffect(true) {
        viewModel.getGameById(id!!)
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            state.game?.let { game ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Text(
                                text = game.title,
                                fontSize = 26.sp,
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            AsyncImage(
                                model = game.thumbnail, contentDescription = game.title,
                                placeholder = painterResource(id = R.drawable.ftg)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.description) + " ${game.shortDescription}",
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.genre) + " ${game.genre}",
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.platform) + " ${game.platform}",
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.developer) + " ${game.developer}",
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.publisher) + " ${game.publisher}",
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.release_date) + " ${game.releaseDate.toFormattedDate()}",
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.game_url),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Card(
                            border = BorderStroke(1.dp, ButtonColor),
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                                val status = viewModel.status.value
                                if (status == Status.Available) {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(game.gameUrl)
                                    )
                                    context.startActivity(intent)
                                } else {
                                    viewModel.showSnackBar(noConnection)
                                }
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = ButtonColor
                            )
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = stringResource(id = R.string.open_site),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.free_to_game_url),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Card(
                            border = BorderStroke(1.dp, ButtonColor),
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                                val status = viewModel.status.value
                                if (status == Status.Available) {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(game.freeToGameProfileUrl)
                                    )
                                    context.startActivity(intent)
                                } else {
                                    viewModel.showSnackBar(noConnection)
                                }
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = ButtonColor
                            )
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = stringResource(id = R.string.open_site),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }

                }
            }

        }
    }
}

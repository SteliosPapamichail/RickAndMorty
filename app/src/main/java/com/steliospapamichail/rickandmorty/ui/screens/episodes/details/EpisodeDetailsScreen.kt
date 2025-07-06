package com.steliospapamichail.rickandmorty.ui.screens.episodes.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.steliospapamichail.rickandmorty.R
import com.steliospapamichail.rickandmorty.domain.models.episodes.EpisodeDetails
import org.koin.androidx.compose.koinViewModel

@Composable
fun EpisodeDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: EpisodeDetailsViewModel = koinViewModel(),
    episodeId: Int,
    onCharacterSelected: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = episodeId) {
        viewModel.fetchEpisodeDetails(episodeId)
    }

    when(val state = uiState) {
        is EpisodeDetailsUIState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.msg)
            }
        }
        EpisodeDetailsUIState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is EpisodeDetailsUIState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EpisodeInfoHeader(episodeDetails = state.details)
                }
                items(state.details.characterIds) { id ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                            .clickable { onCharacterSelected(id) }
                    ) {
                        Text(
                            text = id.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EpisodeInfoHeader(modifier: Modifier = Modifier, episodeDetails: EpisodeDetails) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            //todo:sp extract to reusable component
            modifier = Modifier
                .wrapContentWidth()
                .height(150.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = episodeDetails.episodeCode, modifier = Modifier.padding(horizontal = 20.dp))
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = episodeDetails.title,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.aired_on, episodeDetails.airedOn),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        HorizontalDivider()
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.episde_characters_count_label, episodeDetails.characterIds.size),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Start
        )
    }
}

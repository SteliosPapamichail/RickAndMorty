package com.steliospapamichail.rickandmorty.ui.screens.episodes.overview

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.steliospapamichail.rickandmorty.R
import com.steliospapamichail.rickandmorty.ui.screens.episodes.components.EpisodePreviewItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodesOverviewScreen(
    modifier: Modifier = Modifier,
    viewModel: EpisodesOverviewViewModel = koinViewModel(),
    onEpisodeSelected: (Int) -> Unit,
) {
    val context = LocalContext.current
    val lastRefreshTimestamp by viewModel.lastRefreshTimestamp.collectAsState(null)
    val episodeItems = viewModel.episodePreviewsPaged.collectAsLazyPagingItems()
    val appendState by remember { derivedStateOf { episodeItems.loadState.append } }
    val endOfListReached by remember { derivedStateOf { appendState is LoadState.NotLoading && appendState.endOfPaginationReached } }
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    LaunchedEffect(key1 = episodeItems.loadState.refresh) {
        if (episodeItems.loadState.refresh is LoadState.Error) {
            Toast.makeText(context, "Error: ${(episodeItems.loadState.refresh as LoadState.Error).error}", Toast.LENGTH_SHORT).show()
        }
    }

    PullToRefreshBox(
        isRefreshing = episodeItems.loadState.refresh is LoadState.Loading,
        onRefresh = {
            episodeItems.refresh()
            viewModel.updateLastRefreshTimestamp()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        if (episodeItems.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LazyVerticalGrid(
                    modifier = modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    columns = GridCells.Fixed(if (isLandscape) 3 else 2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (lastRefreshTimestamp != null) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .background(
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    text = stringResource(R.string.last_updated_on, lastRefreshTimestamp!!),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    items(
                        count = episodeItems.itemCount,
                        key = episodeItems.itemKey { it.code },
                        contentType = episodeItems.itemContentType { "Episode" }
                    ) { index ->
                        val episodePreview = episodeItems[index]
                        if (episodePreview != null) {
                            EpisodePreviewItem(episodePreview = episodePreview) {
                                onEpisodeSelected(it)
                            }
                        }
                    }

                    if (episodeItems.loadState.append is LoadState.Loading) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            // currently appending items after reaching bottom of screen
                            CircularProgressIndicator()
                        }
                    }

                    if (endOfListReached) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "No more episodes available : )",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
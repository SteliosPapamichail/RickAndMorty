package com.steliospapamichail.rickandmorty.ui.screens.episodes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.steliospapamichail.rickandmorty.R
import com.steliospapamichail.rickandmorty.domain.models.episodes.EpisodePreview
import com.steliospapamichail.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun EpisodePreviewItem(modifier: Modifier = Modifier, episodePreview: EpisodePreview, onClick: (Int) -> Unit) {
    Column(
        modifier.clickable { onClick(episodePreview.id) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(150.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                )
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = episodePreview.code)
        }
        Text(text = episodePreview.title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Text(
            text = stringResource(R.string.aired_on, episodePreview.airDate),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun EpisodeItemPreview(modifier: Modifier = Modifier) {
    RickAndMortyTheme {
        EpisodePreviewItem(
            modifier.fillMaxWidth(), EpisodePreview(
                id = 0,
                title = "Mayhem",
                airDate = "10/5/2017",
                code = "S0E0"
            )
        ) {}
    }
}
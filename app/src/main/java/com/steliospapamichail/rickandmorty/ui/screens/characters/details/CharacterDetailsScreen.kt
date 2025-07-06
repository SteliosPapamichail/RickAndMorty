package com.steliospapamichail.rickandmorty.ui.screens.characters.details

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.steliospapamichail.rickandmorty.R
import com.steliospapamichail.rickandmorty.extensions.showToast
import com.steliospapamichail.rickandmorty.ui.components.CircularLoader
import com.steliospapamichail.rickandmorty.ui.components.ErrorSection
import com.steliospapamichail.rickandmorty.utils.FileExtensions
import com.steliospapamichail.rickandmorty.utils.MimeTypes
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterDetailsScreen(modifier: Modifier = Modifier, viewModel: CharacterDetailsViewModel = koinViewModel(), charId: Int) {
    val uiState by viewModel.uiState.collectAsState()
    val uiEvents = viewModel.uiEvents
    val context = LocalContext.current
    val dirPicker = rememberLauncherForActivityResult(
        contract = CreateDocument(MimeTypes.TXT_FILE_MIME_TYPE),
        onResult = { uri ->
            if (uri == null) {
                context.showToast(R.string.file_save_error)
                return@rememberLauncherForActivityResult
            }
            viewModel.exportCharacter(uri)
        }
    )


    LaunchedEffect(key1 = charId) {
        viewModel.fetchCharacterDetails(charId)
    }

    LaunchedEffect(uiEvents) {
        uiEvents.collect { event ->
            when (event) {
                is CharacterDetailsEvent.ExportError -> context.showToast(event.errorResId)
                is CharacterDetailsEvent.ExportSuccess -> context.showToast(event.msgResId)
            }
        }
    }

    //note: reason for 'state' declaration: allows the compiler to perform automatic smart casting, another approach is to just cast manually and use that instance
    when (val state = uiState) {
        is CharacterDetailsUIState.Loading -> {
            CircularLoader(modifier)
        }

        is CharacterDetailsUIState.Error -> {
            ErrorSection(modifier, state.msg)
        }

        is CharacterDetailsUIState.Success -> {
            Column(
                modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CharacterInfoHeader(
                    imageUrl = state.details.imageUrl,
                    name = state.details.name,
                    episodesCount = state.details.episodesCount
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CharacterTrait(
                        modifier = Modifier.weight(0.25f),
                        label = stringResource(R.string.character_trait_type_label),
                        trait = state.details.type
                    )
                    CharacterTrait(
                        modifier = Modifier.weight(0.25f),
                        label = stringResource(R.string.character_trait_gender_label),
                        trait = state.details.gender
                    )
                    CharacterTrait(
                        modifier = Modifier.weight(0.25f),
                        label = stringResource(R.string.character_trait_status_label),
                        trait = state.details.status
                    )
                    CharacterTrait(
                        modifier = Modifier.weight(0.25f),
                        label = stringResource(R.string.character_trait_species_label),
                        trait = state.details.species
                    )
                }
                state.details.origin?.let {
                    CharacterLocation(label = stringResource(R.string.character_origin_label), location = it.name)
                }
                state.details.location?.let {
                    CharacterLocation(
                        label = stringResource(R.string.character_location_label),
                        location = it.name
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        dirPicker.launch(state.details.name + FileExtensions.TXT_FILE_EXTENSION)
                    }
                ) {
                    Text(text = stringResource(R.string.character_info_export_btn_label))
                }
            }
        }
    }
}

@Composable
private fun CharacterInfoHeader(imageUrl: String, name: String, episodesCount: Int) {
    AsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.character_image_content_desc),
        modifier = Modifier
            .size(92.dp)
            .clip(CircleShape)
    )
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = name,
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
    Text(text = pluralStringResource(R.plurals.appears_in_episodes, episodesCount, episodesCount))
}

@Composable
private fun CharacterLocation(modifier: Modifier = Modifier, label: String, location: String) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = MaterialTheme.typography.titleLarge)
            Text(text = location, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun CharacterTrait(modifier: Modifier = Modifier, label: String, trait: String) {
    if (trait.isEmpty()) return

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Box(
            Modifier
                .width(100.dp)
                .height(30.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp)),
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                text = trait,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
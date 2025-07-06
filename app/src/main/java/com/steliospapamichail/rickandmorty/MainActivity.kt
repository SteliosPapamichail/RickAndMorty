package com.steliospapamichail.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.steliospapamichail.rickandmorty.ui.nav.Route
import com.steliospapamichail.rickandmorty.ui.screens.characters.details.CharacterDetailsScreen
import com.steliospapamichail.rickandmorty.ui.screens.episodes.details.EpisodeDetailsScreen
import com.steliospapamichail.rickandmorty.ui.screens.episodes.overview.EpisodesOverviewScreen
import com.steliospapamichail.rickandmorty.ui.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRouteTitle by remember {
                derivedStateOf {
                    Route.getTitleFromQualifiedName(currentBackStackEntry?.destination?.route?.substringBeforeLast("/"))
                }
            }

            RickAndMortyTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(text = stringResource(currentRouteTitle), style = MaterialTheme.typography.titleLarge)
                            },
                            navigationIcon = {
                                if (currentBackStackEntry?.destination?.hasRoute(Route.EpisodesOverview::class) == true) return@CenterAlignedTopAppBar

                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_back_arrow),
                                        contentDescription = stringResource(R.string.navigate_back_content_desc)
                                    )
                                }
                            },
                            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
                        )
                    }) { innerPadding ->
                    NavHost(navController = navController, startDestination = Route.EpisodesOverview) {
                        composable<Route.EpisodesOverview> {
                            EpisodesOverviewScreen(Modifier.padding(innerPadding)) { episodeId ->
                                navController.navigate(Route.EpisodeDetails(episodeId))
                            }
                        }
                        composable<Route.EpisodeDetails> { backstackEntry ->
                            val episodeDetails: Route.EpisodeDetails = backstackEntry.toRoute()
                            EpisodeDetailsScreen(modifier = Modifier.padding(innerPadding), episodeId = episodeDetails.id) { charId ->
                                navController.navigate(Route.CharacterDetails(charId))
                            }
                        }
                        composable<Route.CharacterDetails> { backStackEntry ->
                            val characterDetails: Route.CharacterDetails = backStackEntry.toRoute()
                            CharacterDetailsScreen(modifier = Modifier.padding(innerPadding), charId = characterDetails.id)
                        }
                    }
                }
            }
        }
    }
}
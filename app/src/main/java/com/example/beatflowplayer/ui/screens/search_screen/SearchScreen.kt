package com.example.beatflowplayer.ui.screens.search_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.beatflowplayer.domain.enums.SearchCategory
import com.example.beatflowplayer.ui.screens.albums_screen.AlbumCard
import com.example.beatflowplayer.ui.screens.artists_screen.ArtistCard
import com.example.beatflowplayer.ui.screens.tracks_screen.TrackCard
import com.example.beatflowplayer.viewmodel.PlayerViewModel
import com.example.beatflowplayer.viewmodel.SearchViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    playerViewModel: PlayerViewModel,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val query by searchViewModel.query.collectAsState()
    val selectedCategories by searchViewModel.selectedCategories.collectAsState()
    val tracks by searchViewModel.tracks.collectAsState()
    val albums by searchViewModel.albums.collectAsState()
    val artists by searchViewModel.artists.collectAsState()
    val currentTrack by playerViewModel.currentTrack

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    var hasFocus by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }.also { source ->
        LaunchedEffect(source) {
            source.interactions.collect { interaction ->
                when (interaction) {
                    is FocusInteraction.Focus -> hasFocus = true
                    is FocusInteraction.Unfocus -> hasFocus = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focusRequester.freeFocus()
            }
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .height(110.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Search",
                fontWeight = FontWeight.W600,
                fontSize = 22.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "BackButton",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { state ->
                            hasFocus = state.isFocused
                        },
                    value = query,
                    interactionSource = interactionSource,
                    onValueChange = { searchViewModel.updateQuery(it) },
                    singleLine = true,
                    placeholder = { Text("Search by tracks, albums etc..") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchCategory.entries.forEach { category ->
                val isSelected = selectedCategories.contains(category)

                FilterChip(
                    selected = isSelected,
                    onClick = { searchViewModel.toggleSelectedCategory(category) },
                    label = { Text(category.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        val showResults = query.isNotBlank() &&
                selectedCategories.isNotEmpty() &&
                (tracks.isNotEmpty() || albums.isNotEmpty() || artists.isNotEmpty())

        if (!showResults) {
            AnimatedContent(
                targetState = when {
                    query.isBlank() -> 1
                    query.isNotBlank() && selectedCategories.isEmpty() -> 2
                    tracks.isEmpty() && albums.isEmpty() && artists.isEmpty() -> 3
                    else -> 0
                },
                transitionSpec = {
                    (slideInVertically { height -> height / 4 } + fadeIn())
                        .togetherWith(slideOutVertically { height -> -height / 4 } + fadeOut())
                }
            ) { state ->
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (state) {
                            1 -> "Enter a query to start searching"
                            2 -> "Select at least one category"
                            else -> "Nothing found"
                        }
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = if (currentTrack != null) 96.dp else 0.dp)
            ) {
                if (SearchCategory.TRACKS in selectedCategories && tracks.isNotEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInHorizontally(),
                            exit = fadeOut() + slideOutVertically()
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 16.dp),
                                text = "Tracks",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    items(tracks) { track ->
                        AnimatedVisibility(
                            visible = true,
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            TrackCard(
                                track = track,
                                isCurrent = false,
                                playerViewModel = playerViewModel
                            ) {

                            }
                        }
                    }
                }

                if (albums.isNotEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(8.dp),
                                thickness = 3.dp
                            )
                        }
                    }
                }

                if (SearchCategory.ALBUMS in selectedCategories && albums.isNotEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
                                text = "Albums",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    val albumRows = albums.chunked(2)
                    items(albumRows) { row ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Row {
                                row.forEach { album ->
                                    AlbumCard(
                                        album = album,
                                        navController = navController,
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                if (row.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                if (artists.isNotEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(8.dp),
                                thickness = 3.dp
                            )
                        }
                    }
                }

                if (SearchCategory.ARTISTS in selectedCategories && artists.isNotEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
                                text = "Artists",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    items(artists) { artist ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                ArtistCard(
                                    artist
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(navController = rememberNavController(), hiltViewModel())
}
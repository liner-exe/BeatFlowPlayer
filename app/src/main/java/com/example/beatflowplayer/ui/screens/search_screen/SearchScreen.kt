package com.example.beatflowplayer.ui.screens.search_screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
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

//    val interactionSource = remember { MutableInteractionSource() }
//    val isFocused by interactionSource.collectIsFocusedAsState()
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


    val animatedPadding by animateDpAsState(
        targetValue = if (hasFocus) 64.dp else 16.dp,
        label = "paddingAnimation",
        animationSpec = tween(300)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                focusRequester.freeFocus()
            }
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            IconButton(
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 8.dp),
                onClick = {
                    navController.popBackStack()
                }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "BackButton",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 32.dp, bottom = 8.dp, end = 8.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { state ->
                        hasFocus = state.isFocused
                    },
                value = query,
                interactionSource = interactionSource,
                onValueChange = { searchViewModel.updateQuery(it) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchCategory.entries.forEach { category ->
                val isSelected = selectedCategories.contains(category)

                FilterChip(
                    selected = isSelected,
                    onClick = { searchViewModel.toggleSelectedCategory(category) },
                    label = { Text(category.label) }
                )
            }
        }

        if (query.isBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = animatedPadding),
                    text = "Enter a query to start searching.",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W400
                )
            }
        }
        else if (tracks.isEmpty() && albums.isEmpty() && artists.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = animatedPadding),
                    text = "Nothing found.",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W400
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = if (currentTrack != null) 96.dp else 0.dp)
            ) {
                if (SearchCategory.TRACKS in selectedCategories && tracks.isNotEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            text = "Tracks",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    items(tracks) { track ->
                        TrackCard(
                            track = track,
                            isCurrent = false
                        ) {

                        }
                    }
                }

                if (SearchCategory.ALBUMS in selectedCategories && albums.isNotEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
                            text = "Albums",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    val albumRows = albums.chunked(2)
                    items(albumRows) { row ->
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

                    if (SearchCategory.ARTISTS in selectedCategories && artists.isNotEmpty()) {
                        item {
                            Text(
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
                                text = "Artists",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        items(artists) { artist ->
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
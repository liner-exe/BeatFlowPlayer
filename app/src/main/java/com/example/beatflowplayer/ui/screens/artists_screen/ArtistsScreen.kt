package com.example.beatflowplayer.ui.screens.artists_screen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.beatflowplayer.viewmodel.ArtistViewModel

@Composable
fun ArtistsScreen(
    isSelected: Boolean,
    navController: NavHostController,
    artistViewModel: ArtistViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        artistViewModel.loadArtists()
    }

    val artists by artistViewModel.artists
    val isLoading by artistViewModel.isArtistsLoading

    if (isLoading && artists.isEmpty()) {
        CircularProgressIndicator()
    } else {
        ArtistsList(artists, navController)
    }
}

@Preview
@Composable
fun ArtistsScreenPreview() {
    ArtistsScreen(false, rememberNavController())
}
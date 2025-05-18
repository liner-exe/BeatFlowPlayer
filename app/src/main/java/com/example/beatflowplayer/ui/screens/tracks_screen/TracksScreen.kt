package com.example.beatflowplayer.ui.screens.tracks_screen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.beatflowplayer.viewmodel.PlayerViewModel

@Composable
fun TracksScreen(
    playerViewModel: PlayerViewModel
) {
    val tracks = playerViewModel.allTracks
    val isLoading by playerViewModel.isLoading

    if (isLoading) CircularProgressIndicator()
    else TrackList(tracks, playerViewModel)
}

@Preview
@Composable
fun TracksScreenPreview() {
    TracksScreen(hiltViewModel())
}
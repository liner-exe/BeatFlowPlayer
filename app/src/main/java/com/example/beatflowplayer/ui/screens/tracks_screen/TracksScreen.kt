package com.example.beatflowplayer.ui.screens.tracks_screen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.beatflowplayer.viewmodel.PlayerViewModel

@Composable
fun TracksScreen(
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val tracks = playerViewModel.allTracks
    val isLoading = playerViewModel.isLoading

//    LaunchedEffect(Unit) {
//        playerViewModel.setQueue(tracks, 0)
//    }

    if (isLoading.value) CircularProgressIndicator()
    else TrackList(tracks)
}

@Preview
@Composable
fun TracksScreenPreview() {
    TracksScreen()
}
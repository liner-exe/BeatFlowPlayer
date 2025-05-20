package com.example.beatflowplayer.ui.screens.player_screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.beatflowplayer.ui.screens.tracks_screen.TrackCard
import com.example.beatflowplayer.viewmodel.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueContent(
    onDismissRequest: () -> Unit,
    playerViewModel: PlayerViewModel
) {
    val queue = playerViewModel.trackQueue

    val currentTrack = playerViewModel.currentTrack

    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        LazyColumn {
            items(queue.value) { track ->
                TrackCard(
                    track,
                    track.id == currentTrack.value?.id,
                    playerViewModel = playerViewModel
                ) {

                }
            }
        }
    }
}

@Preview
@Composable
fun QueueContentPreview() {
    QueueContent({}, hiltViewModel())
}
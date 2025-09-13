package com.example.beatflowplayer.ui.screens.album_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.beatflowplayer.domain.model.Track
import com.example.beatflowplayer.domain.model.player.QueueContext
import com.example.beatflowplayer.domain.model.player.SourceType
import com.example.beatflowplayer.ui.screens.tracks_screen.TrackCard
import com.example.beatflowplayer.viewmodel.AlbumViewModel
import com.example.beatflowplayer.viewmodel.PlayerViewModel

@Composable
fun AlbumScreen(
    id: String,
    navController: NavHostController,
    albumViewModel: AlbumViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel
) {
    LaunchedEffect(Unit) {
        albumViewModel.loadAlbumById(id.toLong())
    }

    val album by albumViewModel.album
    val currentTrack by playerViewModel.currentTrack
    val queueContext by playerViewModel.queueContext

    val isCurrentTrack = { track: Track ->
        track.id == currentTrack?.id && when(val source = queueContext?.source) {
            is SourceType.Album -> source.id == album?.id
            else -> false
        }
    }

    if (album != null) {
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (currentTrack != null) 96.dp else 0.dp)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            item { HeaderSection(album!!, navController, playerViewModel = playerViewModel) }
            item {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 12.dp),
                    thickness = 3.dp
                )
            }
            items(album!!.tracks) { track ->
                TrackCard(
                    track = track,
                    isCurrent = isCurrentTrack(track),
                    playerViewModel = playerViewModel
                ) {
                    val context = QueueContext(
                        album!!.tracks,
                        SourceType.Album(album!!.id, album!!.title)
                    )
                    playerViewModel.playFromContext(context, track.id)
                }
            }
        }
    } else {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun AlbumScreenPreview() {
    // AlbumScreen("1", navController = rememberNavController(), hiltViewModel())
}
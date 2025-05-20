package com.example.beatflowplayer.ui.screens.artist_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.beatflowplayer.domain.model.Track
import com.example.beatflowplayer.domain.model.player.QueueContext
import com.example.beatflowplayer.domain.model.player.SourceType
import com.example.beatflowplayer.ui.screens.albums_screen.AlbumCard
import com.example.beatflowplayer.ui.screens.tracks_screen.TrackCard
import com.example.beatflowplayer.viewmodel.ArtistViewModel
import com.example.beatflowplayer.viewmodel.PlayerViewModel

@Composable
fun ArtistScreen(
    id: String,
    navController: NavHostController,
    artistViewModel: ArtistViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel
) {
    LaunchedEffect(Unit) {
        artistViewModel.loadArtistById(id.toLong())
    }

    val artist by artistViewModel.artist
    val currentTrack by playerViewModel.currentTrack
    val queueContext by playerViewModel.queueContext

    val isCurrentTrack = { track: Track ->
        track.id == currentTrack?.id && when (val source = queueContext?.source) {
            is SourceType.Artist -> source.id == artist?.id
            else -> false
        }
    }

    LaunchedEffect(Unit) {
        Log.d("ArtistScreen", queueContext.toString())
    }

    if (artist != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (currentTrack != null) 96.dp else 0.dp)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            item { HeaderSection(artist!!, navController, playerViewModel) }
            item {
                if (artist!!.albums.isNotEmpty()) {
                    HorizontalDivider(
                        Modifier
                            .padding(vertical = 8.dp),
                        thickness = 3.dp
                    )
                }
            }

            val albumRows = artist!!.albums.chunked(2)
            items(albumRows) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
            item {
                if (artist!!.tracks.isNotEmpty()) {
                    HorizontalDivider(
                        Modifier
                            .padding(vertical = 8.dp),
                        thickness = 3.dp
                    )
                }
            }
            items(artist!!.tracks) { track ->
                TrackCard(
                    track = track,
                    isCurrent = isCurrentTrack(track),
                    playerViewModel = playerViewModel
                ) {
                    val context = QueueContext(
                        artist!!.tracks, SourceType.Artist(artist!!.id, artist!!.name)
                    )
                    playerViewModel.playFromContext(context, track.id)
                }
            }
        }
    }
}
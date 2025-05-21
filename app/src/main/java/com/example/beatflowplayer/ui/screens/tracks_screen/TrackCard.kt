package com.example.beatflowplayer.ui.screens.tracks_screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beatflowplayer.R
import com.example.beatflowplayer.domain.model.Track
import com.example.beatflowplayer.domain.model.player.QueueContext
import com.example.beatflowplayer.domain.model.player.SourceType
import com.example.beatflowplayer.ui.widget.queue.SoundWaveIndicator
import com.example.beatflowplayer.utils.getAlbumCover
import com.example.beatflowplayer.viewmodel.PlayerViewModel

@Composable
fun TrackCard(
    track: Track,
    isCurrent: Boolean,
    playerViewModel: PlayerViewModel,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null, track) {
        value = getAlbumCover(context, track.uri, 50, 50)
    }

    val isPlaying by playerViewModel.isPlaying

    Card(
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 16.dp,
                    horizontal = 2.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))

            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Cover",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.default_cover),
                    contentDescription = "Cover",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isCurrent) {
                        SoundWaveIndicator(
                            isPlaying = isPlaying
                        )
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.85f),
                        text = track.title,
                        color = when (isCurrent) {
                            true -> MaterialTheme.colorScheme.primary
                            false -> MaterialTheme.colorScheme.onBackground
                        },
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        modifier = Modifier
                            .padding(end = 16.dp),
                        painter = painterResource(R.drawable.more_horizontal),
                        contentDescription = "More"
                    )
                }

                Text(
                    text = track.artist,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun TrackList(
    tracks: List<Track>,
    playerViewModel: PlayerViewModel
) {
    val currentTrack by playerViewModel.currentTrack
    val queueContext by playerViewModel.queueContext

    LaunchedEffect(Unit) {
        Log.d("TrackList", queueContext.toString())
    }

    val isTrackCurrent = { track: Track ->
        track.id == currentTrack?.id && queueContext?.source is SourceType.AllTracks
    }

    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
        ) {
            items(tracks) { track ->
                TrackCard(
                    track = track,
                    isCurrent = isTrackCurrent(track),
                    playerViewModel = playerViewModel
                ) {
                    val context = QueueContext(tracks, SourceType.AllTracks)
                    playerViewModel.playFromContext(context, track.id)
                    playerViewModel.play()
                }
            }
        }
    }
}
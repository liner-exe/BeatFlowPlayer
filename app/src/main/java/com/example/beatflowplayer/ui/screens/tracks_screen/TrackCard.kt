package com.example.beatflowplayer.ui.screens.tracks_screen

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null, track) {
        value = getAlbumCover(context, track.uri, 64, 64)
    }

    val isPlaying by playerViewModel.isPlaying

    OutlinedCard(
        modifier = modifier
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = { onClick() },
        border = BorderStroke(1.dp, if (isCurrent) Color.White else
            MaterialTheme.colorScheme.onSecondaryContainer.copy(0.3f))
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isCurrent) {
//                        Brush.horizontalGradient(
//                            listOf(
//                                Color(0xFF3949AB),
//                                Color(0xFF6C63FF)
//                            )
//                        )
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Transparent)
                        )
                    } else {
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Transparent)
                        )
                    }
                )
                .padding(vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.85f),
                        text = track.artist,
                        color = if (isCurrent) Color.LightGray else
                            MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    modifier = Modifier
                        .padding(end = 16.dp),
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More"
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

    val isTrackCurrent = { track: Track ->
        track.id == currentTrack?.id && queueContext?.source is SourceType.AllTracks
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1C1B2F),
                        Color(0xFF2B284A)
                    )
                )
            )
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
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
package com.example.beatflowplayer.ui.screens.artist_screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.beatflowplayer.R
import com.example.beatflowplayer.domain.model.Artist
import com.example.beatflowplayer.domain.model.player.QueueContext
import com.example.beatflowplayer.domain.model.player.SourceType
import com.example.beatflowplayer.utils.getAlbumCover
import com.example.beatflowplayer.viewmodel.PlayerViewModel

@Composable
fun HeaderSection(
    artist: Artist,
    navController: NavHostController,
    playerViewModel: PlayerViewModel
) {
    val context = LocalContext.current

    val bitmap by produceState<Bitmap?>(
        initialValue = null,
        if (artist.tracks.isNotEmpty()) artist.tracks[0] else null
    ) {
        value = getAlbumCover(context, artist.tracks[0].uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "BackButton")
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { }
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "EditButton")
            }

            IconButton(
                onClick = { }
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "MoreButton")
            }
        }

        if (bitmap != null) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(shape = RoundedCornerShape(15.dp)),
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "PlaylistCover",
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(shape = RoundedCornerShape(15.dp)),
                painter = painterResource(R.drawable.default_cover),
                contentDescription = "PlaylistCover",
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 32.dp)
        ) {
            Text(
                text = artist.name,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    val context = QueueContext(
                        artist.tracks, SourceType.Artist(artist.id, artist.name)
                    )
                    playerViewModel.playFromContext(context, -1)
                    playerViewModel.play()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = "PlayIcon"
                )

                Spacer(modifier = Modifier.size(5.dp))

                Text(
                    text = "Play",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))

            Button(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.shuffle),
                    contentDescription = "PlayIcon"
                )

                Spacer(modifier = Modifier.size(5.dp))

                Text(
                    text = "Shuffle",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
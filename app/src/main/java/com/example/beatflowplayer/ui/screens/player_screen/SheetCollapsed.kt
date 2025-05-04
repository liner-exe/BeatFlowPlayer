package com.example.beatflowplayer.ui.screens.player_screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.beatflowplayer.R
import com.example.beatflowplayer.utils.getAlbumCover
import com.example.beatflowplayer.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetCollapsedContent(
    progress: Float,
    scaffoldState: BottomSheetScaffoldState,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val track by playerViewModel.currentTrack
    val isPlaying by playerViewModel.isPlaying
    val position by playerViewModel.position
    val duration by playerViewModel.duration
    val accentColor by playerViewModel.accentColor

    if (track == null) return

    val bitmap by produceState<Bitmap?>(initialValue = null, track!!.uri) {
        value = getAlbumCover(context, track!!.uri)
    }

    val progressRatio = if (duration > 0) position.toFloat() / duration else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha = 1f - progress)
            .clickable {
                scope.launch {
                    scaffoldState.bottomSheetState.expand()
                }
            }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.onSecondaryContainer)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressRatio.toFloat())
                        .fillMaxHeight()
                        .background(accentColor ?: MaterialTheme.colorScheme.primary)
                )
            }

            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (bitmap != null) {
                        Image(
                            modifier = Modifier
                                .size(64.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = "AlbumCover"
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "NO IMAGE",
                                fontSize = 4.sp,
                                color = Color.White
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                    ) {
                        Text(
                            text = track!!.title,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Text(
                            text = track!!.artist,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            playerViewModel.togglePlayPause()
                        },
                        modifier = Modifier
                            .size(96.dp)
                            .padding(8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(48.dp),
                            painter = painterResource(
                                when (isPlaying) {
                                    true -> R.drawable.pause
                                    false -> R.drawable.play_button
                                }
                            ),
                            contentDescription = "PlayButton",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}
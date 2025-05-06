package com.example.beatflowplayer.ui.screens.player_screen

import android.graphics.Bitmap
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import com.example.beatflowplayer.R
import com.example.beatflowplayer.utils.convertFromDuration
import com.example.beatflowplayer.utils.getAlbumCover
import com.example.beatflowplayer.viewmodel.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetExpandedContent(
    progress: Float,
    scaffoldState: BottomSheetScaffoldState,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isUserInteractionWithSlider by remember { mutableStateOf(false) }
    val animatedPosition = remember { Animatable(0f) }

    val isPlaying by playerViewModel.isPlaying
    val duration by playerViewModel.duration
    val currentTrack by playerViewModel.currentTrack
    val currentPosition by playerViewModel.position
    val accentColor by playerViewModel.accentColor

    if (currentTrack == null) return

    val bitmap by produceState<Bitmap?>(initialValue = null, currentTrack!!.uri) {
        value = getAlbumCover(context, currentTrack!!.uri)
    }

    var shuffleColor by remember { mutableStateOf(Color.White) }
    var loopColor by remember { mutableStateOf(Color.White) }

    val scope = rememberCoroutineScope()

    var isQueueShowed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        snapshotFlow { currentPosition }
            .distinctUntilChanged()
            .collect {

                if (!isUserInteractionWithSlider)
                    sliderPosition = it.toFloat()
            }
    }

    LaunchedEffect(sliderPosition, isUserInteractionWithSlider) {
        if (!isUserInteractionWithSlider) {
            animatedPosition.animateTo(
                targetValue = sliderPosition,
                animationSpec = tween(200)
            )
        } else {
            animatedPosition.snapTo(sliderPosition)
        }
    }

    val currentTimeText by remember(sliderPosition) {
        derivedStateOf {
            convertFromDuration(sliderPosition.toLong())
        }
    }

    val remainingTimeText by remember(sliderPosition, currentTrack) {
        derivedStateOf {
            val remaining = (duration - sliderPosition.toLong()).coerceAtLeast(0L)
            "-${convertFromDuration(remaining)}"
        }
    }

    LaunchedEffect(currentTrack) {
        animatedPosition.snapTo(0f)
        playerViewModel.setAccentColor(null)
    }

    LaunchedEffect(bitmap) {
        bitmap?.let {
            val palette = withContext(Dispatchers.Default) {
                Palette.from(it).generate()
            }

            playerViewModel.setAccentColor(palette.vibrantSwatch?.rgb?.let { Color(it) })
        }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            accentColor ?: Color.White,
            accentColor?.copy(alpha = 0.7f) ?: Color.White,
            Color.Black.copy(alpha = 0.9f)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .alpha(alpha = progress),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Row {
                    IconButton(
                        modifier = Modifier
                            .size(48.dp),
                        onClick = {
                            scope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_down),
                            contentDescription = "CollapseButton",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        modifier = Modifier
                            .size(48.dp),
                        onClick = {}
                    ) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "CollapseButton",
                            tint = Color.White
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = "Album Cover",
                            modifier = Modifier
                                .size(300.dp)
                                .clip(shape = RoundedCornerShape(12.dp))
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(300.dp)
                                .clip(shape = RoundedCornerShape(12.dp))
                                .background(color = Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "NO IMAGE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        Text(
                            text = currentTrack!!.title,
                            color = Color.White,
                            fontSize = 22.sp,
                            style = TextStyle(
                                fontWeight = FontWeight.SemiBold
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = currentTrack!!.artist,
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            style = TextStyle(
                                fontWeight = FontWeight.W500
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    Slider(
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        value = animatedPosition.value,
                        onValueChange = { newValue ->
                            sliderPosition = newValue
                            isUserInteractionWithSlider = true
                        },
                        onValueChangeFinished = {
                            isUserInteractionWithSlider = false

                            playerViewModel.seekTo(sliderPosition.toLong())

                            scope.launch {
                                delay(300)
                            }
                        },
                        valueRange = 0f..duration.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Transparent,
                            activeTrackColor = Color.Transparent,
                            inactiveTrackColor = Color.Transparent
                        ),
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                            )
                        },
                        track = { sliderPositions ->
                            val value = sliderPositions.value
                            val valueRange = sliderPositions.valueRange
                            val fraction = ((value - valueRange.start) / (valueRange.endInclusive -
                                    valueRange.start)).coerceIn(0f, 1f)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Color.Gray,
                                            RoundedCornerShape(50.dp)
                                        )
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(fraction)
                                        .background(
                                            Color.White,
                                            RoundedCornerShape(50.dp)
                                        )
                                )
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = currentTimeText,
                            color = Color.LightGray
                        )

                        Text(
                            text = remainingTimeText,
                            color = Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                playerViewModel.toggleShuffle()

                                if (shuffleColor == Color.White) shuffleColor =
                                    accentColor ?: Color.Green
                                else shuffleColor = Color.White
                            },
                            Modifier
                                .clip(shape = CircleShape)
                                .size(36.dp)
                                .background(color = Color.Transparent)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.shuffle),
                                contentDescription = "Shuffle",
                                modifier = Modifier.size(36.dp),
                                tint = shuffleColor
                            )
                        }

                        IconButton(
                            onClick = {
                                playerViewModel.playPrevious()
                            },
                            Modifier
                                .clip(shape = CircleShape)
                                .size(48.dp)
                                .background(color = Color.Transparent)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.previous),
                                contentDescription = "Previous",
                                modifier = Modifier.size(36.dp),
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = {
                                playerViewModel.togglePlayPause()
                            },
                            Modifier
                                .clip(shape = CircleShape)
                                .size(64.dp)
                                .background(color = Color.White)
                        ) {
                            Icon(
                                painter = painterResource(
                                    when (isPlaying) {
                                        true -> R.drawable.pause
                                        false -> R.drawable.play_button
                                    }
                                ),
                                contentDescription = "PlayButton",
                                tint = Color.Black
                            )
                        }

                        IconButton(
                            onClick = {
                                playerViewModel.playNext()
                            },
                            Modifier
                                .clip(shape = CircleShape)
                                .size(48.dp)
                                .background(color = Color.Transparent)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.next),
                                contentDescription = "Next",
                                modifier = Modifier
                                    .size(36.dp),
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = {
                                playerViewModel.toggleLoop()

                                loopColor = if (loopColor.value == Color.White.value) {
                                    accentColor ?: Color.Green
                                } else {
                                    Color.White
                                }
                            },
                            Modifier
                                .clip(shape = CircleShape)
                                .size(48.dp)
                                .background(color = Color.Transparent)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.repeat),
                                contentDescription = "Repeat",
                                modifier = Modifier.size(36.dp),
                                tint = loopColor
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("placeholder")
                        IconButton(
                            onClick = { isQueueShowed = true }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.List,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }

    if (isQueueShowed) {
        QueueContent(onDismissRequest = { isQueueShowed = false })
    }
}
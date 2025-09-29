package com.example.beatflowplayer.ui.screens.albums_screen

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.beatflowplayer.R
import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.navigation.Screen
import com.example.beatflowplayer.utils.getAlbumCover

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumCard(
    album: Album,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null, album) {
        value = getAlbumCover(context, album.artworkUri, 128, 128)
    }

    var isPinned by remember { mutableStateOf(album.isPinned) }
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(album.isPinned) {
        isPinned = album.isPinned
    }

    Card(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { showMenu = true },
                    onTap = {
                        navController?.navigate(Screen.AlbumScreen.withId(album.id.toString()))
                    }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(164.dp),
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(15.dp)),
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "playlistCover"
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(15.dp)),
                        painter = painterResource(R.drawable.default_cover),
                        contentDescription = "playlistCover"
                    )
                }

                if (isPinned) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(24.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.Black.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Default.Star,
                            contentDescription = "pinned",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 10.dp, top = 4.dp, bottom = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clipToBounds()
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            animationMode = MarqueeAnimationMode.Immediately,
                            repeatDelayMillis = 500,
                            initialDelayMillis = 0,
                            spacing = MarqueeSpacing(10.dp),
                            velocity = 20.dp
                        ),
                    text = album.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clipToBounds()
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            animationMode = MarqueeAnimationMode.Immediately,
                            repeatDelayMillis = 500,
                            initialDelayMillis = 0,
                            spacing = MarqueeSpacing(10.dp),
                            velocity = 20.dp
                        ),
                    text = album.artist,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }

    val sheetState = rememberModalBottomSheetState()

    if (showMenu) {
        ModalBottomSheet(
            onDismissRequest = { showMenu = false },
            sheetState = sheetState
        ) {
            LazyColumn {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isPinned = !isPinned
                            }
                            .padding(vertical = 16.dp, horizontal = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null
                        )
                        Text(text = if (!album.isPinned) "Pin" else "Unpin")
                    }
                }
            }
        }
    }
}

@Composable
fun AlbumsList(
    albums: List<Album>,
    navController: NavHostController?
) {
    val albumsSorted by remember(albums) {
        mutableStateOf(albums.sortedBy { !it.isPinned })
    }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1C1B2F),
                        Color(0xFF2B284A)
                    )
                )
            ),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(albumsSorted) { album ->
            AlbumCard(
                album = album,
                navController = navController,
            )
        }
    }
}

@Composable
@Preview
fun AlbumCardPreview() {
    AlbumCard(
        Album(0, "Million Dollar Business", "Morgen", 0, "", emptyList()),
        rememberNavController(), hiltViewModel()
    )
}
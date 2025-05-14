package com.example.beatflowplayer.ui.screens.albums_screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.beatflowplayer.R
import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.ui.navigation.Screen
import com.example.beatflowplayer.utils.getAlbumCover

@Composable
fun AlbumCard(
    album: Album,
    navController: NavHostController? = null
) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null, album) {
        value = getAlbumCover(context, album.artworkUri)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        onClick = {
            navController?.navigate(Screen.AlbumScreen.route)
        },
        shape = RectangleShape
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        modifier = Modifier
                            .size(164.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "playlistCover"
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(164.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        painter = painterResource(R.drawable.nominalo),
                        contentDescription = "playlistCover"
                    )
                }
            }

            Text(
                modifier = Modifier.padding(start = 30.dp),
                text = album.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                modifier = Modifier.padding(bottom = 4.dp, start = 30.dp),
                text = album.artists.joinToString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun AlbumsList(
    albums: List<Album>,
    navController: NavHostController?
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(albums) { album ->
            AlbumCard(album = album, navController = navController)
        }
    }
}

@Composable
@Preview
fun AlbumCardPreview() {
    AlbumCard(Album(0, "Million Dollar Business", listOf("Morgen"), "", emptyList()))
}
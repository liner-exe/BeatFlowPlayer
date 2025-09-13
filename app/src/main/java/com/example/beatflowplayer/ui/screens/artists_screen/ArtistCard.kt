package com.example.beatflowplayer.ui.screens.artists_screen

import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.beatflowplayer.R
import com.example.beatflowplayer.domain.model.Artist
import com.example.beatflowplayer.domain.navigation.Screen
import com.example.beatflowplayer.ui.screens.albums_screen.AlbumCard
import com.example.beatflowplayer.utils.getAlbumCover

@Composable
fun ArtistCard(
    artist: Artist,
    navController: NavHostController? = null
) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null, artist) {
        value = if (artist.tracks.isNotEmpty()) {
            getAlbumCover(context, artist.tracks[0].uri)
        } else {
            null
        }
    }

    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSecondaryContainer.copy(0.3f)),
        onClick = {
            navController?.navigate(Screen.ArtistScreen.withId(artist.id.toString()))
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            if (bitmap != null) {
                Image(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(shape = CircleShape),
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Artist`s cover",
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(shape = CircleShape),
                    painter = painterResource(R.drawable.default_cover),
                    contentDescription = "Artist`s cover",
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp),
                    text = artist.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ArtistsList(
    artists: List<Artist>,
    navController: NavHostController?
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF1C1B2F),
                    Color(0xFF2B284A)
                )
            ))
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(artists) { artist ->
            ArtistCard(artist = artist, navController = navController)
        }
    }
}

@Preview
@Composable
fun ArtistCardPreview() {
    ArtistCard(artist = Artist(
        id = 1,
        name = "MORGENSHTERN",
        emptyList(),
        emptyList()
    )
    )
}
package com.example.beatflowplayer.ui.screens.playlists_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.beatflowplayer.R
import com.example.beatflowplayer.domain.navigation.Screen

@Composable
fun PlaylistCard(
    playlist: Playlist,
    navController: NavHostController? = null
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        onClick = {
            navController?.navigate(Screen.PlaylistScreen.route) {
                restoreState = true
            }
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
                Image(
                    modifier = Modifier
                        .size(164.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    painter = painterResource(R.drawable.default_cover),
                    contentDescription = "playlistCover",
                    contentScale = ContentScale.Inside
                )
            }

            Text(
                modifier = Modifier.padding(bottom = 4.dp, start = 30.dp),
                text = playlist.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun PlayList(
    playlists: List<Playlist>,
    navController: NavHostController? = null
) {
    LazyVerticalGrid(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.secondaryContainer),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(playlists) { playlist ->
            PlaylistCard(playlist = playlist, navController = navController)
        }
    }
}

@Composable
@Preview
fun PlaylistCardPreview() {
    PlaylistCard(Playlist(0, "Name", emptyList()))
}
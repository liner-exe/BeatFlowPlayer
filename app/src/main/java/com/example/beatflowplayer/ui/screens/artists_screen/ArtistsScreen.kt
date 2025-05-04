package com.example.beatflowplayer.ui.screens.artists_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.beatflowplayer.R

@Composable
fun ArtistsScreen(
    navController: NavHostController? = null
) {
    val artists = listOf(
        Artist(0, "Morgen"),
        Artist(1, "MORGENSHTERN"),
        Artist(2, "И его вторая личность"),
        Artist(3, "И третья"),
        Artist(4, "И четвёртая"),
        Artist(5, "И пятая"),
        Artist(6, "Надоело считать"),
        Artist(7, "Поэтому"),
        Artist(8, "MORGEN"),
        Artist(9, "MORGEN"),
        Artist(10, "MORGEN"),
        Artist(11, "MORGEN"),
        Artist(12, "MORGEN"),
        Artist(13, "MORGEN"),
        Artist(14, "MORGEN"),
    )

    ArtistsList(artists, navController)
}

@Preview
@Composable
fun ArtistsScreenPreview() {
    ArtistsScreen()
}
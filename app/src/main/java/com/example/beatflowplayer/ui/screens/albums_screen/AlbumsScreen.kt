package com.example.beatflowplayer.ui.screens.albums_screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.beatflowplayer.ui.screens.playlists_screen.PlaylistsScreen
import com.example.beatflowplayer.utils.getAlbums

@Composable
fun AlbumsScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val albums = getAlbums(context)

    AlbumsList(albums, navController)
}

@Preview
@Composable
fun AlbumsScreenPreview() {
    AlbumsScreen(navController = rememberNavController())
}
package com.example.beatflowplayer.ui.screens.albums_screen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.beatflowplayer.viewmodel.AlbumViewModel

@Composable
fun AlbumsScreen(
    navController: NavHostController,
    albumViewModel: AlbumViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        albumViewModel.loadAlbums()
    }

    val albums by albumViewModel.albums
    val isLoading by albumViewModel.isAlbumsLoading

    if (isLoading && albums.isEmpty()) {
        CircularProgressIndicator()
    } else {
        AlbumsList(albums, navController)
    }
}

@Preview
@Composable
fun AlbumsScreenPreview() {
    AlbumsScreen(navController = rememberNavController())
}
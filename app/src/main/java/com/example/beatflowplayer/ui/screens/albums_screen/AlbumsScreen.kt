package com.example.beatflowplayer.ui.screens.albums_screen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.repository.AudioRepository
import com.example.beatflowplayer.ui.screens.playlists_screen.PlaylistsScreen
import com.example.beatflowplayer.utils.getAlbums
import com.example.beatflowplayer.viewmodel.AlbumViewModel

@Composable
fun AlbumsScreen(
    navController: NavHostController,
    albumViewModel: AlbumViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        albumViewModel.loadAlbums()
    }

    val albums = albumViewModel.albums
    val isLoading = albumViewModel.isAlbumsLoading

    if (isLoading.value && albums.value!!.isEmpty()) {
        CircularProgressIndicator()
    } else {
        AlbumsList(albums.value!!, navController)
    }
}

@Preview
@Composable
fun AlbumsScreenPreview() {
    AlbumsScreen(navController = rememberNavController())
}
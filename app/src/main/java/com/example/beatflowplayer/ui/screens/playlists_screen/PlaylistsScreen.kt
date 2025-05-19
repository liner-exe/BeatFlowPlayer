package com.example.beatflowplayer.ui.screens.playlists_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.beatflowplayer.ui.screens.albums_screen.AlbumCard
import java.nio.file.WatchEvent

@Composable
fun PlaylistsScreen(navController: NavHostController? = null) {
    val playlists = emptyList<Playlist>()
    
    Box(modifier = Modifier.fillMaxSize()) {
        PlayList(playlists, navController)

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "You don't have any playlist.")
        }
//        Box(modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center) {
//            CircularProgressIndicator()
//        }

        Button(
            onClick = {},
            modifier = Modifier
                .size(96.dp)
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "AddIcon"
            )
        }
    }
}

@Preview
@Composable
fun PlaylistsScreenPreview() {
    PlaylistsScreen()
}
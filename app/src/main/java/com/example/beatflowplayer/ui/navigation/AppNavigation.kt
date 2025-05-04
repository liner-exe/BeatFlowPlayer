package com.example.beatflowplayer.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.beatflowplayer.ui.screens.album_screen.AlbumScreen
import com.example.beatflowplayer.ui.screens.player_screen.AudioPlayerUI
import com.example.beatflowplayer.ui.screens.playlist_screen.PlaylistScreen
import com.example.beatflowplayer.ui.screens.search_screen.SearchScreen
import com.example.beatflowplayer.ui.screens.settings_screen.SettingsScreen
import com.example.beatflowplayer.ui.widget.BottomSheetOverlay


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        content = { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.PlayerScreen.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.PlayerScreen.route) { AudioPlayerUI(navController) }
                composable(Screen.SearchScreen.route) { SearchScreen(navController) }
                composable(Screen.PlaylistScreen.route) { PlaylistScreen(navController) }
                composable(Screen.AlbumScreen.route) { AlbumScreen(navController) }
                composable(Screen.SettingsScreen.route) { SettingsScreen(navController) }
            }

            BottomSheetOverlay()
        }
    )
}

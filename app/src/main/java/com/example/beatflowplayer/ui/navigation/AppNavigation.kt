package com.example.beatflowplayer.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.beatflowplayer.domain.navigation.Screen
import com.example.beatflowplayer.ui.screens.album_screen.AlbumScreen
import com.example.beatflowplayer.ui.screens.artist_screen.ArtistScreen
import com.example.beatflowplayer.ui.screens.player_screen.AudioPlayerUI
import com.example.beatflowplayer.ui.screens.playlist_screen.PlaylistScreen
import com.example.beatflowplayer.ui.screens.search_screen.SearchScreen
import com.example.beatflowplayer.ui.screens.settings_screen.SettingsScreen
import com.example.beatflowplayer.ui.widget.BottomSheetOverlay
import com.example.beatflowplayer.viewmodel.PlayerViewModel


@Composable
fun AppNavigation(
    playerViewModel: PlayerViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        content = { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.PlayerScreen.route,
                modifier = Modifier.padding(paddingValues),
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(200)
                    )
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(500)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(600, easing = FastOutSlowInEasing)
                    )
                },
            ) {
                composable(Screen.PlayerScreen.route) {
                    AudioPlayerUI(navController, playerViewModel)
                }
                composable(Screen.SearchScreen.route) {
                    SearchScreen(navController, playerViewModel)
                }
                composable(Screen.PlaylistScreen.route) { PlaylistScreen(navController) }
                composable(
                    Screen.AlbumScreen.route,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    AlbumScreen(
                        id = backStackEntry.arguments?.getString("id") ?: "-1",
                        navController = navController,
                        playerViewModel = playerViewModel
                    )
                }
                composable(
                    Screen.ArtistScreen.route,
                    arguments = listOf(navArgument("id") {type = NavType.StringType })
                ) { backStackEntry ->
                    ArtistScreen(
                        id = backStackEntry.arguments?.getString("id") ?: "-1",
                        navController = navController,
                        playerViewModel = playerViewModel
                    )
                }
                composable(Screen.SettingsScreen.route) { SettingsScreen(navController) }
            }

            BottomSheetOverlay(navController = navController, playerViewModel = playerViewModel)
        }
    )
}

package com.example.beatflowplayer.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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


@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation(
    playerViewModel: PlayerViewModel
) {
    val navController = rememberNavController()
    val animDuration = 300

    Scaffold(
        content = { paddingValues ->
            SharedTransitionLayout {
                NavHost(
                    navController = navController,
                    startDestination = Screen.PlayerScreen.route,
                    modifier = Modifier.fillMaxSize(),
                    enterTransition = {
                        slideIntoContainer(
                            towards = SlideDirection.Start,
                            animationSpec = tween(animDuration, easing = FastOutSlowInEasing)
                        ) + fadeIn(
                            animationSpec = tween(
                                animDuration,
                                easing = FastOutSlowInEasing
                            )
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = SlideDirection.Start,
                            animationSpec = tween(animDuration, easing = FastOutSlowInEasing),
                            targetOffset = { fullDistance -> fullDistance / 4 }
                        ) + fadeOut(
                            animationSpec = tween(
                                animDuration,
                                easing = FastOutSlowInEasing
                            )
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            towards = SlideDirection.End,
                            animationSpec = tween(animDuration, easing = FastOutSlowInEasing),
                            initialOffset = { fullDistance -> fullDistance / 4 }
                        ) + fadeIn(
                            animationSpec = tween(
                                animDuration,
                                easing = FastOutSlowInEasing
                            )
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            towards = SlideDirection.End,
                            animationSpec = tween(animDuration, easing = FastOutSlowInEasing)
                        ) + fadeOut(
                            animationSpec = tween(
                                animDuration,
                                easing = FastOutSlowInEasing
                            )
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
                        arguments = listOf(navArgument("id") { type = NavType.StringType })
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
        }
    )
}
package com.example.beatflowplayer

sealed class Screen(val route: String) {
    object PlayerScreen : Screen("player_screen")
    object TracksScreen : Screen("tracks_screen")
}
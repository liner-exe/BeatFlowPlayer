package com.example.beatflowplayer.ui.navigation

sealed class Screen(val route: String) {
    object PlayerScreen : Screen("player_screen")
    object NavBarScreen : Screen("navbar_screen")
    object TracksScreen : Screen("tracks_screen")
    object PlaylistsScreen : Screen("playlists_screen")
    object AlbumsScreen : Screen("albums_screen")
    object ArtistsScreen : Screen("artists_screen")
    object SearchScreen : Screen("search_screen")
    object PlaylistScreen : Screen("playlist_screen")
    object AlbumScreen : Screen("album_screen")
    object SettingsScreen : Screen("settings_screen")
}
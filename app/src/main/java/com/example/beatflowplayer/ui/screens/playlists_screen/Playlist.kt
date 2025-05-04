package com.example.beatflowplayer.ui.screens.playlists_screen

import com.example.beatflowplayer.domain.model.Track

data class Playlist(
    val id: Long,
    val title: String,
    val tracks: List<Track>
)
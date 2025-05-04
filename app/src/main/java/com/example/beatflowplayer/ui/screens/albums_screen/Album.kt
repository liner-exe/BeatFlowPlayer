package com.example.beatflowplayer.ui.screens.albums_screen

import com.example.beatflowplayer.domain.model.Track

class Album(
    val id: Long,
    val title: String,
    val artist: String,
    val tracks: List<Track>
)
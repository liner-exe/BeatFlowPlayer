package com.example.beatflowplayer.domain.model

data class Artist(
    val id: Long,
    val name: String,
    val albums: List<Album>,
    val tracks: List<Track>
)
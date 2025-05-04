package com.example.beatflowplayer.domain.model

import androidx.core.net.toUri

data class TrackEntity(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val uri: String,
    val isFavourite: Boolean = false
) {
    fun toTrack(): Track = Track(
        id = id,
        title = title,
        artist = artist,
        duration = duration,
        uri = uri.toUri(),
        isFavourite = isFavourite
    )
}
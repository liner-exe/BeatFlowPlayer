package com.example.beatflowplayer.domain.model

data class Album(
    val id: Long,
    val tracks: List<Track>
) {
    // fun getFirstSong(): Track = tracks.firstOrNull() ?: Track.emptyTrack()
}

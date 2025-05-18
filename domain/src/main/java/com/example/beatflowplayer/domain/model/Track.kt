package com.example.beatflowplayer.domain.model

data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val uri: String,
    val albumId: Long,
    val artistId: Long,
    val isFavourite: Boolean = false
) {
    fun formattedDuration(): String {
        val minutes = duration / 1000 / 60
        val seconds = (duration / 1000) % 60
        return "%d:%02d".format(minutes, seconds)
    }

    override fun equals(other: Any?): Boolean = other is Track && id == other.id
    override fun hashCode(): Int = id.hashCode()

    companion object {
        val UNKNOWN_ARTIST: String = "Unknown Artist"

        val emptyTrack = Track(
            id = -1,
            title = "",
            artist = "",
            duration = -1,
            uri = "",
            albumId = -1,
            artistId = -1
        )
    }
}
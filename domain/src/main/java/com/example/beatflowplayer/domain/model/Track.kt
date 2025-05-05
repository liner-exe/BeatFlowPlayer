package com.example.beatflowplayer.domain.model

data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val uri: String,
    val isFavourite: Boolean = false
) {
    fun formattedDuration(): String {
        val minutes = duration / 1000 / 60
        val seconds = (duration / 1000) % 60
        return "%d:%02d".format(minutes, seconds)
    }

    override fun equals(other: Any?): Boolean {
        return id == (other as Track).id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + uri.hashCode()
        return result
    }

    companion object {
        val UNKNOWN_ARTIST: String = "Unknown Artist"

        val emptyTrack = Track(
            id = -1,
            title = "",
            artist = "",
            duration = -1,
            uri = ""
        )
    }
}
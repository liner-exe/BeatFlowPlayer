package com.example.beatflowplayer.domain.model

import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MediaItem

data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val uri: Uri,
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
            uri = "".toUri()
        )
    }
}

fun Track.toMediaItem(): MediaItem {
    val metadata = MediaMetadata.Builder()
        .setTitle(title)
        .setArtist(artist)
        .build()

    return MediaItem.Builder()
        .setMediaId(id.toString())
        .setUri(uri)
        .setMediaMetadata(metadata)
        .build()
}

fun MediaItem.toTrack(): Track {
    val metadata = mediaMetadata

    return Track(
        id = mediaId.toLong(),
        title = metadata.title.toString(),
        artist = metadata.artist.toString(),
        duration = metadata.durationMs?.toLong() ?: 0L,
        uri = localConfiguration!!.uri
    )
}
package com.example.beatflowplayer.data.mapper

import androidx.core.os.bundleOf
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MediaItem
import com.example.beatflowplayer.domain.model.Track

fun Track.toMediaItem(): MediaItem {
    val metadata = MediaMetadata.Builder()
        .setTitle(title)
        .setArtist(artist)
        .setExtras(
            bundleOf("albumId" to albumId)
        )
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
        duration = metadata.durationMs ?: 0L,
        albumId = metadata.extras!!.getLong("albumId", -1),
        uri = localConfiguration!!.uri.toString(),
    )
}
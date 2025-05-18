package com.example.beatflowplayer.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beatflowplayer.domain.model.Track

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "artist")
    val artist: String,

    @ColumnInfo(name = "duration")
    val duration: Long,

    @ColumnInfo(name = "uri")
    val uri: String,

    @ColumnInfo(name = "album_id")
    val albumId: Long,

    @ColumnInfo(name = "artist_id")
    val artistId: Long,

    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean = false
) {
    fun toTrack(): Track = Track(
        id = id,
        title = title,
        artist = artist,
        duration = duration,
        uri = uri,
        albumId = albumId,
        artistId = artistId,
        isFavourite = isFavourite
    )
}
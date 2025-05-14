package com.example.beatflowplayer.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beatflowplayer.domain.model.Album

@Entity(tableName = "album")
data class AlbumEntity(
    @PrimaryKey val id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "artists")
    val artists: String,

    @ColumnInfo(name = "artwork")
    val artworkUri: String,

    @ColumnInfo(name = "track_ids")
    val tracksIds: String
) {
    fun toAlbum() = Album(
        id = id,
        title = title,
        artists = listOf(artists),
        artworkUri = artworkUri,
        tracks = emptyList()
    )
}
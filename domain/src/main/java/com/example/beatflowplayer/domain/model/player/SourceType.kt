package com.example.beatflowplayer.domain.model.player

import com.example.beatflowplayer.domain.navigation.Screen

sealed class SourceType {
    object AllTracks : SourceType()
    data class Playlist(val id: Long, val name: String) : SourceType()
    data class Album(val id: Long, val name: String) : SourceType()
    data class Artist(val id: Long, val name: String) : SourceType()

    fun getSourceId(): Long = when (this) {
        is AllTracks -> -1
        is Playlist -> id
        is Album -> id
        is Artist -> id
    }

    fun displayName(): String = when (this) {
        is AllTracks -> "Now Playing"
        is Playlist -> name
        is Album -> name
        is Artist -> name
    }

    fun getRouteForSource(id: String): String = when (this) {
        is AllTracks -> ""
        is Playlist -> ""
        is Album -> Screen.AlbumScreen.withId(id)
        is Artist -> Screen.ArtistScreen.withId(id)
    }
}
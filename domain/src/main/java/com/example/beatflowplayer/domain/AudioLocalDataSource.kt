package com.example.beatflowplayer.domain

import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.model.Artist
import com.example.beatflowplayer.domain.model.Playlist
import com.example.beatflowplayer.domain.model.Track

interface AudioLocalDataSource {
    suspend fun getAllTracks(): List<Track>
    suspend fun getAllAlbums(): List<Album>
    suspend fun getAllArtists(): List<Artist>
    suspend fun getAllPlaylists(): List<Playlist>
}
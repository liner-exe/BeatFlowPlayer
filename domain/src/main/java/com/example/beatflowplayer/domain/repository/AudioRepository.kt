package com.example.beatflowplayer.domain.repository

import com.example.beatflowplayer.domain.model.Track

interface AudioRepository {
    suspend fun getAllTracks(): List<Track>
    suspend fun getAllAlbums(): List<Track>
    suspend fun getAllArtists(): List<Track>
    suspend fun getAllPlaylists(): List<Track>

    suspend fun getTracksByAlbumId(albumId: Long): List<Track>
    suspend fun getTracksByArtistId(artistId: Long): List<Track>
    suspend fun getTracksByPlaylistId(playlistId: Long): List<Track>
}
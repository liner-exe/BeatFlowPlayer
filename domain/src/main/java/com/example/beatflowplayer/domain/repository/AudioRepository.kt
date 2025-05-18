package com.example.beatflowplayer.domain.repository

import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.model.Artist
import com.example.beatflowplayer.domain.model.Track

interface AudioRepository {
    suspend fun getAllTracks(): List<Track>
    suspend fun getAllAlbums(): List<Album>
    suspend fun getAllArtists(): List<Artist>
    suspend fun getAllPlaylists(): List<Track>

    suspend fun getAlbumById(albumId: Long): Album?
    suspend fun getArtistById(artistId: Long): Artist?

    suspend fun getTracksByAlbumId(albumId: Long): List<Track>?
    suspend fun getTracksByArtistId(artistId: Long): List<Track>?
    suspend fun getTracksByPlaylistId(playlistId: Long): List<Track>

    suspend fun refreshAll()
}
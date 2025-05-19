package com.example.beatflowplayer.domain.repository

import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.model.Artist
import com.example.beatflowplayer.domain.model.Track
import javax.management.Query

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

    suspend fun searchTracks(query: String): List<Track>
    suspend fun searchAlbums(query: String): List<Album>
    suspend fun searchArtists(query: String): List<Artist>

    suspend fun refreshAll()
}
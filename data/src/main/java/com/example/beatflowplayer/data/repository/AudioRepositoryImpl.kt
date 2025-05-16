package com.example.beatflowplayer.data.repository

import android.R
import android.util.Log
import com.example.beatflowplayer.domain.AudioLocalDataSource
import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.model.Track
import com.example.beatflowplayer.domain.repository.AudioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepositoryImpl @Inject constructor(
    private val audioLocalDataSource: AudioLocalDataSource
) : AudioRepository {
    private var cachedTracks: List<Track>? = null
    private var cachedAlbums: List<Album>? = null

    override suspend fun getAllTracks(): List<Track> {
        if (cachedTracks != null) return cachedTracks!!

        return withContext(Dispatchers.IO) {
            val result = audioLocalDataSource.getAllTracks()
            cachedTracks = result
            result
        }
    }

    override suspend fun getAllAlbums(): List<Album> {
        if (cachedAlbums != null) return cachedAlbums!!

        return withContext(Dispatchers.IO) {
            if (cachedTracks == null) {
                cachedTracks = audioLocalDataSource.getAllTracks()
            }

            val tracksByAlbum = cachedTracks!!.groupBy { it.albumId }
            val rawAlbums = audioLocalDataSource.getAllAlbums()

            val albums = rawAlbums.map { album ->
                album.copy(
                    tracks = tracksByAlbum[album.id] ?: emptyList()
                )
            }
            cachedAlbums = albums
            albums
        }
    }

    override suspend fun getAllArtists(): List<Track> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPlaylists(): List<Track> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbumById(albumId: Long): Album? {
        return cachedAlbums?.firstOrNull { it.id == albumId }
    }

    override suspend fun getTracksByAlbumId(albumId: Long): List<Track>? {
        return cachedTracks?.filter { it.albumId == albumId }
    }

    override suspend fun getTracksByArtistId(artistId: Long): List<Track> {
        TODO("Not yet implemented")
    }

    override suspend fun getTracksByPlaylistId(playlistId: Long): List<Track> {
        TODO("Not yet implemented")
    }
}
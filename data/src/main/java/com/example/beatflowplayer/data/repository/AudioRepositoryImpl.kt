package com.example.beatflowplayer.data.repository

import android.util.Log
import com.example.beatflowplayer.domain.AudioLocalDataSource
import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.model.Artist
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
    private var cachedArtists: List<Artist>? = null

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
            val tracks = cachedTracks ?: audioLocalDataSource.getAllTracks().also { cachedTracks = it }

            val rawAlbums = audioLocalDataSource.getAllAlbums()
            val albums = buildAlbumsFrom(rawAlbums, tracks)

            cachedAlbums = albums
            albums
        }
    }

    private fun buildAlbumsFrom(rawAlbums: List<Album>, tracks: List<Track>): List<Album> {
        val tracksByAlbum = tracks.groupBy { it.albumId }

        return rawAlbums
            .distinctBy { it.id }
            .map { album ->
                album.copy(tracks = tracksByAlbum[album.id] ?: emptyList())
            }
    }

    override suspend fun getAllArtists(): List<Artist> {
        if (cachedArtists != null) return cachedArtists!!

        return try {
            val tracks = cachedTracks ?: audioLocalDataSource.getAllTracks().also { cachedTracks = it }
            val albums = cachedAlbums ?: audioLocalDataSource.getAllAlbums().also { cachedAlbums = it }

            val artists = buildArtistsFrom(tracks, albums)
            cachedArtists = artists
            artists
        } catch (e: Exception) {
            Log.e("AudioRepository", "Error in getAllArtists", e)
            emptyList()
        }
    }

    private fun buildArtistsFrom(tracks: List<Track>, albums: List<Album>): List<Artist> {
        val artistIds = (tracks.map { it.artistId } + albums.map { it.artistId }).distinct()
        val tracksByArtist = tracks.groupBy { it.artistId }
        val albumsByArtist = albums.groupBy { it.artistId }

        return artistIds.map { artistId ->
            val tracks = tracksByArtist[artistId] ?: emptyList()
            val albums = albumsByArtist[artistId] ?: emptyList()
            val name = tracks.firstOrNull()?.artist
                ?: albums.firstOrNull()?.artist
                ?: Track.UNKNOWN_ARTIST

            Artist(
                id = artistId,
                name = name,
                tracks = tracks,
                albums = albums
            )
        }
    }

    override suspend fun getAllPlaylists(): List<Track> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbumById(albumId: Long): Album? {
        return cachedAlbums?.firstOrNull { it.id == albumId }
    }

    override suspend fun getArtistById(artistId: Long): Artist? {
        return cachedArtists?.firstOrNull { it.id == artistId }
    }

    override suspend fun getTracksByAlbumId(albumId: Long): List<Track>? {
        return cachedTracks?.filter { it.albumId == albumId }
    }

    override suspend fun getTracksByArtistId(artistId: Long): List<Track>? {
        return cachedTracks?.filter { it.artistId == artistId }
    }

    override suspend fun getTracksByPlaylistId(playlistId: Long): List<Track> {
        TODO("Not yet implemented")
    }

    override suspend fun searchTracks(query: String): List<Track> {
        val normalizedQuery = query.trim().lowercase()
        val tracks = cachedTracks ?: audioLocalDataSource.getAllTracks().also { cachedTracks = it }

        return try {
            tracks.filter {
                it.title.lowercase().contains(normalizedQuery)
                        || it.artist.lowercase().contains(normalizedQuery)
                        || getAlbumById(it.albumId)?.title?.lowercase()!!
                    .contains(normalizedQuery)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun searchAlbums(query: String): List<Album> {
        val normalizedQuery = query.trim().lowercase()
        val albums = cachedAlbums ?: audioLocalDataSource.getAllAlbums().also { cachedAlbums = it }

        return albums.filter {
            it.title.lowercase().contains(normalizedQuery)
                    || it.artist.lowercase().contains(normalizedQuery)
        }
    }

    override suspend fun searchArtists(query: String): List<Artist> {
        val normalizedQuery = query.trim().lowercase()
        val artists = cachedArtists ?: audioLocalDataSource.getAllArtists().also { cachedArtists = it }

        return artists.filter {
            it.name.lowercase().contains(normalizedQuery)
        }
    }

    override suspend fun refreshAll() {
        withContext(Dispatchers.IO) {
            val tracks = audioLocalDataSource.getAllTracks()
            val rawAlbums = audioLocalDataSource.getAllAlbums()
            val albums = buildAlbumsFrom(rawAlbums, tracks)
            val artists = buildArtistsFrom(tracks, albums)

            cachedTracks = tracks
            cachedAlbums = albums
            cachedArtists = artists
        }
    }
}
package com.example.beatflowplayer.data.local

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.example.beatflowplayer.domain.AudioLocalDataSource
import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.model.Artist
import com.example.beatflowplayer.domain.model.Playlist
import com.example.beatflowplayer.domain.model.Track
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioLocalDataSource {
    override suspend fun getAllTracks(): List<Track> {
        return withContext(Dispatchers.IO) {
            try {
                val audioList = mutableListOf<Track>()

                val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

                val projection = arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.ARTIST_ID
                )

                val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
                val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

                val cursor = context.contentResolver.query(
                    collection,
                    projection,
                    selection,
                    null,
                    sortOrder
                )

                cursor?.use {
                    val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                    val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                    val artistIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)

                    while (it.moveToNext()) {
                        val filePath = it.getString(dataColumn)
                        if (filePath.endsWith(".opus")) continue

                        val id = it.getLong(idColumn)
                        val name = it.getString(nameColumn)
                        val artist = it.getString(artistColumn)
                        val duration = it.getLong(durationColumn)
                        val albumId = it.getLong(albumIdColumn)
                        val artistId = it.getLong(artistIdColumn)

                        val contentUri = ContentUris.withAppendedId(collection, id)

                        audioList.add(Track(id, name, artist, duration, contentUri.toString(),
                            albumId, artistId))
                    }
                }
                audioList

            } catch (e: Exception) {
                Log.e("AudioLocalDataSource", "Error loading tracks.", e)
                emptyList()
            }
        }
    }

    override suspend fun getAllAlbums(): List<Album> {
        return withContext(Dispatchers.IO) {
            try {
                val albumList = mutableListOf<Album>()

                val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

                val projection = arrayOf(
                    MediaStore.Audio.Albums.ALBUM_ID,
                    MediaStore.Audio.Albums.ALBUM,
                    MediaStore.Audio.Albums.ARTIST,
                    MediaStore.Audio.Albums.ARTIST_ID,
                    MediaStore.Audio.Albums._ID
                )

                val sortOrder = "${MediaStore.Audio.Albums.ALBUM} ASC"

                val cursor = context.contentResolver.query(
                    collection,
                    projection,
                    null,
                    null,
                    sortOrder
                )

                cursor?.use {
                    val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID)
                    val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
                    val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)
                    val artistIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST_ID)
                    val artIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID)

                    while (it.moveToNext()) {
                        val id = it.getLong(idColumn)
                        val title = it.getString(titleColumn)
                        val artist = it.getString(artistColumn)
                        val artistId = it.getLong(artistIdColumn)
                        val artId = it.getLong(artIdColumn)

                        val artworkUri = ContentUris.withAppendedId(collection, artId)

                        albumList.add(
                            Album(id, title, artist, artistId,
                                artworkUri.toString(), emptyList())
                        )
                    }
                }

                albumList

            } catch (e: Exception) {
                Log.e("AudioLocalDataSource", "Error when loading albums", e)
                emptyList()
            }
        }
    }

    override suspend fun getAllArtists(): List<Artist> {
        return withContext(Dispatchers.IO) {
            try {
                val artistsList = mutableListOf<Artist>()

                val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

                val projection = arrayOf(
                    MediaStore.Audio.Artists._ID,
                    MediaStore.Audio.Artists.ARTIST
                )

                val sortOrder = "${MediaStore.Audio.Artists.ARTIST} ASC"

                val cursor = context.contentResolver.query(
                    collection,
                    projection,
                    null,
                    null,
                    sortOrder
                )

                cursor?.use {
                    val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID)
                    val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)

                    while (it.moveToNext()) {
                        val id = it.getLong(idColumn)
                        val name = it.getString(nameColumn)

                        artistsList.add(Artist(id, name, emptyList(), emptyList()))
                    }
                }

                artistsList
            } catch (e: Exception) {
                Log.e("AudioLocalDataSource", "Error when loading artists", e)
                emptyList()
            }
        }
    }

    override suspend fun getAllPlaylists(): List<Playlist> {
        TODO("Not yet implemented")
    }
}
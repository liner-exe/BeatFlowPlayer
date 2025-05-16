package com.example.beatflowplayer.data.local

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.example.beatflowplayer.domain.AudioLocalDataSource
import com.example.beatflowplayer.domain.model.Album
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

                val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

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

                        val contentUri = ContentUris.withAppendedId(collection, id)

                        audioList.add(Track(id, name, artist, duration, contentUri.toString(),
                            albumId))
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

                val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val projection = arrayOf(
                    MediaStore.Audio.Albums._ID,
                    MediaStore.Audio.Albums.ALBUM,
                    MediaStore.Audio.Albums.ARTIST
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
                    val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID)
                    val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
                    val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)

                    while (it.moveToNext()) {
                        val id = it.getLong(idColumn)
                        val title = it.getString(titleColumn)
                        val artist = it.getString(artistColumn)

                        val artworkUri = ContentUris.withAppendedId(collection, id)

                        albumList.add(
                            Album(id, title, artist.split(", "), artworkUri.toString(), emptyList())
                        )
                    }
                }

                albumList

            } catch (e: Exception) {
                Log.e("AudioLocalDataSource", "Error when loading tracks", e)
                emptyList()
            }
        }
    }

    override suspend fun getAllArtists(): List<Album> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPlaylists(): List<Playlist> {
        TODO("Not yet implemented")
    }
}
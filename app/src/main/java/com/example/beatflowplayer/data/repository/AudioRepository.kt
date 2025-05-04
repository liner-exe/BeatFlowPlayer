package com.example.beatflowplayer.data.repository

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.example.beatflowplayer.data.local.AudioLocalDataSource
import com.example.beatflowplayer.domain.model.Track
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioLocalDataSource: AudioLocalDataSource
) {
    suspend fun getAllAudioTracks(): List<Track> {
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
                    MediaStore.Audio.Media.DATA
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

                    while (it.moveToNext()) {
                        val filePath = it.getString(dataColumn)
                        if (filePath.endsWith(".opus")) continue

                        val id = it.getLong(idColumn)
                        val name = it.getString(nameColumn)
                        val artist = it.getString(artistColumn)
                        val duration = it.getLong(durationColumn)

                        val contentUri = ContentUris.withAppendedId(collection, id)

                        audioList.add(Track(id, name, artist, duration, contentUri))
                    }
                }
                audioList

            } catch (e: Exception) {
                Log.e("AudioRepository", "Error loading tracks.", e)
                emptyList()
            }
        }
    }
}
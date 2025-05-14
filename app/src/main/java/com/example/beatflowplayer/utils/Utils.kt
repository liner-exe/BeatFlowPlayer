package com.example.beatflowplayer.utils

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.model.Track

fun getTracksForAlbum(context: Context, albumId: Long): List<Track> {
    val audioList = mutableListOf<Track>()

    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.ALBUM
    )

    val selection = "${MediaStore.Audio.Media.ALBUM_ID} = ? AND ${MediaStore.Audio.Media.IS_MUSIC} != 0"
    val selectionArgs = arrayOf(albumId.toString())
    val sortOrder = "${MediaStore.Audio.Media.ALBUM} ASC"

    val cursor = context.contentResolver.query(
        collection,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
        val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

        while (it.moveToNext()) {
            val id = it.getLong(idColumn)
            val name = it.getString(nameColumn)
            val artist = it.getString(artistColumn)
            val duration = it.getLong(durationColumn)

            val contentUri = ContentUris.withAppendedId(collection, id)

            audioList.add(Track(id, name, artist, duration, contentUri.toString()))
        }
    }

    return audioList
}

fun getAlbums(context: Context): List<Album> {
    val albumList = mutableListOf<Album>()

    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    val projection = arrayOf(
        MediaStore.Audio.Albums._ID,
        MediaStore.Audio.Albums.ALBUM,
        MediaStore.Audio.Albums.ARTIST,
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
        val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
        val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)

        while (it.moveToNext()) {
            val id = it.getLong(idColumn)
            val name = it.getString(nameColumn)
            val artist = it.getString(artistColumn)

//            val artworkUri = ContentUris.withAppendedId(
//                "content://media/external/audio/albumart".toUri(),
//                id
//            ).toString()

            val artworkUri = ContentUris.withAppendedId(collection, id)
            Log.d("get_albums", artworkUri.toString())

            albumList.add(Album(id, name, artist.split(", ").toList(), artworkUri.toString(), emptyList()))
        }
    }

    return albumList
}

fun getAlbumCover(context: Context, uri: String): Bitmap? {
    val retriever = MediaMetadataRetriever()

    return try {
        retriever.setDataSource(context, uri.toUri())
        val art = retriever.embeddedPicture
        art?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    } catch (e: Exception) {
        null
    } finally {
        retriever.release()
    }
}

fun convertFromDuration(duration: Long): String {
    val minutes = duration / 1000 / 60
    val seconds = (duration / 1000) % 60
    return "%d:%02d".format(minutes, seconds)
}
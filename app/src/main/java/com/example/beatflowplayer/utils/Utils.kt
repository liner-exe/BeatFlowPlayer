package com.example.beatflowplayer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.core.net.toUri

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
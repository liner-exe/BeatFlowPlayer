package com.example.beatflowplayer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getAlbumCover(
    context: Context,
    uri: String,
    maxWidth: Int = 200,
    maxHeight: Int = 200
): Bitmap? = withContext(Dispatchers.IO) {

    val retriever = MediaMetadataRetriever()

    try {
        val pfd = context.contentResolver.openFileDescriptor(uri.toUri(), "r")
            ?: return@withContext null
        pfd.use {
            retriever.setDataSource(it.fileDescriptor)
            val art = retriever.embeddedPicture ?: return@withContext null

            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeByteArray(art, 0, art.size, this)
            }

            val sampleSize = calculateInSampleSize(
                options.outWidth,
                options.outHeight,
                maxWidth,
                maxHeight
            )

            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
            }

            return@withContext BitmapFactory.decodeByteArray(art, 0, art.size, decodeOptions)
        }
    } catch (e: Exception) {
        null
    } finally {
        retriever.release()
    }
}

//    val retriever = MediaMetadataRetriever()
//
//    return try {
//        retriever.setDataSource(context, uri.toUri())
//        val art = retriever.embeddedPicture
//        art?.let {
//            BitmapFactory.decodeByteArray(it, 0, it.size)
//        }
//    } catch (e: Exception) {
//        null
//    } finally {
//        retriever.release()
//    }

fun calculateInSampleSize(
    width: Int,
    height: Int,
    reqWidth: Int,
    reqHeight: Int
): Int {
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while ((halfHeight / inSampleSize) >= reqHeight &&
            (halfWidth / inSampleSize) >= reqWidth
        ) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

fun convertFromDuration(duration: Long): String {
    val minutes = duration / 1000 / 60
    val seconds = (duration / 1000) % 60
    return "%d:%02d".format(minutes, seconds)
}
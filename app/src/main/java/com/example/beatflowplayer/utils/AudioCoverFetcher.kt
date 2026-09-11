package com.example.beatflowplayer.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import coil.ImageLoader
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.request.Options
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.Buffer

class AudioCoverFetcher(
    private val context: Context,
    private val uri: Uri
) : Fetcher {
    override suspend fun fetch(): FetchResult? = withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()

        try {
            val pfd = context.contentResolver.openFileDescriptor(uri, "r") ?: return@withContext null

            pfd.use {
                retriever.setDataSource(it.fileDescriptor)
                val art = retriever.embeddedPicture ?: return@withContext null

                SourceResult(
                    source = ImageSource(
                        source = Buffer().apply { write(art) },
                        context = context
                    ),
                    mimeType = "image/jpeg",
                    dataSource = DataSource.DISK
                )
            }
        } catch (e: Exception) {
            null
        } finally {
            retriever.release()
        }
    }

    class Factory(private val context: Context) : Fetcher.Factory<Uri> {
        override fun create(data: Uri, options: Options, imageLoader: ImageLoader): Fetcher? {
            val scheme = data.scheme
            if (scheme == "content" || scheme == "file") {
                return AudioCoverFetcher(context, data)
            }

            return null
        }
    }
}
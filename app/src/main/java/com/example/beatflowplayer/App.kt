package com.example.beatflowplayer

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.memory.MemoryCache
import com.example.beatflowplayer.utils.AudioCoverFetcher
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(AudioCoverFetcher.Factory(this@App))
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .crossfade(true)
            .build()
    }
}
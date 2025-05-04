package com.example.beatflowplayer.utils

import android.net.Uri

data class MediaItem(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val uri: Uri
)

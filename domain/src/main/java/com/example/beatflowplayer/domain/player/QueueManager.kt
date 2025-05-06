package com.example.beatflowplayer.domain.player

import com.example.beatflowplayer.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface QueueManager {
    val queue: StateFlow<List<Track>>
    val currentTrack: StateFlow<Track?>

    fun setQueue(tracks: List<Track>, index: Int)
    fun addToQueue(track: Track)
    fun skipToNext()
    fun skipToPrevious()
    fun getCurrentIndex(): Int
}
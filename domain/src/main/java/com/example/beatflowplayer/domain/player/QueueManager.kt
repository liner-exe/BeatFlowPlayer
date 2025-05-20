package com.example.beatflowplayer.domain.player

import com.example.beatflowplayer.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface QueueManager {
    val tracks: StateFlow<List<Track>>
    val queue: StateFlow<List<Track>>
    val upNextQueue: StateFlow<List<Track>>
    val currentTrack: StateFlow<Track?>
    val currentIndex: StateFlow<Int>

    fun setQueue(tracks: List<Track>)
    fun setCurrentTrack(index: Int)
    fun addToQueue(track: Track)
    fun updateQueue()
    fun addToUpNext(track: Track)
    fun clearUpNext()
    fun skipToNext()
    fun skipToPrevious()
    fun toggleLoop()
    fun toggleShuffle()
}
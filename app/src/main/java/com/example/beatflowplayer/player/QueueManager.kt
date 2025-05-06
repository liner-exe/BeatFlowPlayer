package com.example.beatflowplayer.player

import com.example.beatflowplayer.domain.model.Track
import com.example.beatflowplayer.domain.player.QueueManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class QueueManagerImp @Inject constructor(): QueueManager {
    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    override val queue: StateFlow<List<Track>> = _queue.asStateFlow()

    private val _currentTrack = MutableStateFlow<Track?>(null)
    override val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    override fun setQueue(tracks: List<Track>, index: Int) {
        _queue.value = tracks
    }

    override fun addToQueue(track: Track) {

    }

    override fun skipToNext() {
        TODO("Not yet implemented")
    }

    override fun skipToPrevious() {
        TODO("Not yet implemented")
    }

    override fun getCurrentIndex(): Int {
        TODO("Not yet implemented")
    }
}
package com.example.beatflowplayer.data.player

import com.example.beatflowplayer.domain.model.Track
import com.example.beatflowplayer.domain.player.QueueManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueueManagerImpl @Inject constructor() : QueueManager {
    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    override val tracks: StateFlow<List<Track>> = _tracks.asStateFlow()

    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    override val queue: StateFlow<List<Track>> = _queue.asStateFlow()

    private val _upNextQueue = MutableStateFlow<List<Track>>(emptyList())
    override val upNextQueue: StateFlow<List<Track>> = _upNextQueue.asStateFlow()

    private val _currentTrack = MutableStateFlow<Track?>(null)
    override val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    private val _currentIndex = MutableStateFlow<Int>(-1)
    override val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _loopMode = MutableStateFlow<LoopMode>(LoopMode.NONE)
    val loopMode: StateFlow<LoopMode> = _loopMode.asStateFlow()

    private val _isShuffle = MutableStateFlow<Boolean>(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()

    enum class LoopMode {
        NONE,
        ONE,
        ALL
    }

    override fun setQueue(tracks: List<Track>) {
        _tracks.value = tracks
        updateQueue()
    }

    override fun setCurrentTrack(index: Int) {
        _currentIndex.value = index
        _currentTrack.value = _queue.value.getOrNull(index)
    }

    override fun addToQueue(track: Track) {
        _queue.update { it + listOf(track) }
    }

    override fun updateQueue() {
        val current = _currentTrack.value
        val baseQueue = if (_isShuffle.value) {
            val otherTracks = _tracks.value.filter { it.id != current?.id }
            val shuffledTracks = otherTracks.shuffled()
            if (current != null) listOf(current) + _upNextQueue.value + shuffledTracks else shuffledTracks
        } else {
            _tracks.value
        }

        val newQueue = if (current != null) {
            val currentIndexInBase = baseQueue.indexOfFirst { it.id == current.id }
            if (currentIndexInBase >= 0) {
                val before = baseQueue.take(currentIndexInBase + 1)
                val after = baseQueue.drop(currentIndexInBase + 1)
                before + _upNextQueue.value + after
            } else {
                baseQueue + _upNextQueue.value
            }
        } else {
            baseQueue + _upNextQueue.value
        }

        _queue.value = newQueue

        val newIndex = newQueue.indexOfFirst { it.id == current?.id }
        _currentIndex.value = if (newIndex >= 0) newIndex else 0
        _currentTrack.value = newQueue.getOrNull(_currentIndex.value)

        if (_loopMode.value == LoopMode.ALL && _currentIndex.value == -1) {
            _currentIndex.value = 0
            _currentTrack.value = newQueue.getOrNull(0)
        }
    }

    override fun addToUpNext(track: Track) {
        _upNextQueue.update { it + track }
        updateQueue()
    }

    override fun clearUpNext() {
        TODO("Not yet implemented")
    }

    override fun skipToNext() {
        val nextIndex = _currentIndex.value + 1
        if (nextIndex < _queue.value.size && _loopMode.value == LoopMode.NONE) {
            _currentIndex.update { nextIndex }
            _currentTrack.update { _queue.value[nextIndex] }
        } else {
            when (_loopMode.value) {
                LoopMode.ALL -> {
                    _currentIndex.update { 0 }
                    _currentTrack.update { _queue.value[0] }
                }
                LoopMode.ONE -> { }
                LoopMode.NONE -> {
                    _currentIndex.update { 0 }
                    _currentTrack.update { _queue.value[0] }
                }
            }
        }
    }

    override fun skipToPrevious() {
        val previousIndex = _currentIndex.value - 1
        if (previousIndex >= 0  && _loopMode.value == LoopMode.NONE) {
            _currentIndex.value = previousIndex
            _currentTrack.value = _queue.value[previousIndex]
        } else {
            when (_loopMode.value) {
                LoopMode.ALL -> {
                    _currentIndex.value = _queue.value.lastIndex
                    _currentTrack.value = _queue.value.lastOrNull()
                }
                LoopMode.ONE -> { }
                LoopMode.NONE -> {
                    _currentIndex.value = _queue.value.lastIndex
                    _currentTrack.value = _queue.value.lastOrNull()
                }
            }
        }
    }

    override fun toggleLoop() {
        _loopMode.value = if (_loopMode.value == LoopMode.NONE) LoopMode.ONE else LoopMode.NONE
    }

    override fun toggleShuffle() {
        _isShuffle.update { !it }
        updateQueue()
    }
}
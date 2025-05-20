package com.example.beatflowplayer.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beatflowplayer.domain.model.Track
import com.example.beatflowplayer.domain.model.player.QueueContext
import com.example.beatflowplayer.domain.model.player.SourceType
import com.example.beatflowplayer.player.PlayerManager
import com.example.beatflowplayer.domain.repository.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.SortedMap
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val playerManager: PlayerManager
) : ViewModel() {
    private val _allTracks = mutableStateListOf<Track>()
    val allTracks: List<Track> get() = _allTracks

    private val _isLoopEnabled = mutableStateOf(false)
    val isLoopEnabled: State<Boolean> = _isLoopEnabled

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isShuffleEnabled = mutableStateOf(false)
    val isShuffleEnabled: State<Boolean> = _isShuffleEnabled

    private val _trackQueue = MutableStateFlow<List<Track>>(emptyList())
    val trackQueue: StateFlow<List<Track>> = _trackQueue.asStateFlow()

    private val _currentIndex = mutableIntStateOf(-1)
    val currentIndex: State<Int> = _currentIndex

    private val _duration = mutableLongStateOf(0L)
    val duration: State<Long> = _duration

    private var _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> = _isPlaying

    private var _currentTrack = mutableStateOf<Track?>(null)
    val currentTrack: State<Track?> = _currentTrack

    private val _currentAlbumId = mutableLongStateOf(-1)
    val currentAlbumId: State<Long> = _currentAlbumId

    private val _queueContext = mutableStateOf<QueueContext?>(null)
    val queueContext: State<QueueContext?> = _queueContext

    private val _playingFrom = mutableStateOf<String?>(null)
    val playingFrom: State<String?> = _playingFrom

    private var _position = mutableLongStateOf(0L)
    val position: State<Long> = _position

    private val _trackPositions = mutableStateMapOf<Long, Int>()
    val trackPositions: Map<Long, Int> = _trackPositions

    private val _timeElapsed = mutableStateOf("0:00")
    val timeElapsed: State<String> = _timeElapsed

    private val _timeRemaining = mutableStateOf("0:00")
    val timeRemaining: State<String> = _timeRemaining

    private val _eventFlow = MutableSharedFlow<String>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _accentColor = mutableStateOf<Color?>(null)
    val accentColor: State<Color?> = _accentColor

    init {
        loadTracks()

        viewModelScope.launch {
            launch {
                playerManager.position.collect { progress ->
                    _position.longValue = progress
                }
            }

            launch {
                playerManager.duration.collect { duration ->
                    _duration.longValue = duration
                }
            }

            launch {
                playerManager.isPlaying.collect { isPlaying ->
                    _isPlaying.value = isPlaying
                }
            }

            launch {
                playerManager.currentTrack.collect { track ->
                    _currentTrack.value = track
                }
            }

            launch {
                playerManager.tracks.collect { tracks ->
                    _trackQueue.value = tracks
                }
            }

            launch {
                playerManager.currentIndex.collect { index ->
                    _currentIndex.value = index
                }
            }
        }
    }

    private fun loadTracks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val tracks = audioRepository.getAllTracks()
                _allTracks.clear()
                _allTracks.addAll(tracks)
            } catch (e: Exception) {
                Log.e("PlayerViewModel", "Error loading tracks.", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun playFromContext(context: QueueContext, trackId: Long) {
        val isSameContext = context.source == _queueContext.value?.source
        _queueContext.value = context

        if (!isSameContext) {
            seekTo(0)
        }

        playerManager.setQueue(context.tracks, trackId)
        play()
    }

    fun play() = playerManager.play()
    fun togglePlayPause() = playerManager.togglePlayPause()
    fun toggleLoop() = playerManager.toggleLoop()
    fun toggleShuffle() = playerManager.toggleShuffle()

    fun seekTo(position: Long) {
        _position.longValue = position
        playerManager.seekTo(position)
    }

    fun playNext() {
        playerManager.playNext()
    }

    fun playPrevious() {
        playerManager.playPrevious()
    }

    fun setAccentColor(color: Color?) {
        _accentColor.value = color
    }

    fun refreshLibrary() {
        viewModelScope.launch {
            audioRepository.refreshAll()
            _eventFlow.emit("Library refreshed")
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
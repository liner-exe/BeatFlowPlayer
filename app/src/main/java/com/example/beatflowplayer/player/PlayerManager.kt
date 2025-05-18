package com.example.beatflowplayer.player

import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import com.example.beatflowplayer.domain.model.Track
import com.example.beatflowplayer.data.mapper.toMediaItem
import com.example.beatflowplayer.data.mapper.toTrack
import com.example.beatflowplayer.data.player.QueueManagerImpl
import com.example.beatflowplayer.domain.player.QueueManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class PlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val queueManager: QueueManager
) {
    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
            shuffleModeEnabled = false
        }
    }

    private val scope = CoroutineScope(Dispatchers.Main)

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _position = MutableStateFlow(0L)
    val position: StateFlow<Long> = _position.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (_isPlaying.value && playbackState == Player.STATE_BUFFERING)
                    _isPlaying.value = true

                if (playbackState == Player.STATE_READY) {
                    _duration.value = exoPlayer.duration.milliseconds.inWholeMilliseconds
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (!isPlaying && exoPlayer.playWhenReady) return
                _isPlaying.value = isPlaying
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val track = mediaItem?.toTrack() ?: return
                val index = queueManager.queue.value.indexOfFirst { it.id == track.id }
                if (index >= 0) {
                    queueManager.setCurrentTrack(index)
                }
            }

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                val updatedTracks = (0 until exoPlayer.mediaItemCount).map { index ->
                    exoPlayer.getMediaItemAt(index).toTrack()
                }
                _tracks.value = updatedTracks
            }
        })

        scope.launch {
            while (isActive) {
                if (exoPlayer.isPlaying) {
                    _position.value = exoPlayer.currentPosition
                }

                delay(500)
            }
        }

        observeQueueManager()
    }

    private fun observeQueueManager() {
        scope.launch {
            queueManager.queue.collect { tracks ->
                val mediaItems = tracks.map { it.toMediaItem() }
                exoPlayer.setMediaItems(mediaItems, queueManager.currentIndex.value, 0)
            }
        }

        scope.launch {
            queueManager.currentIndex.collect { index ->
                if (index >= 0) exoPlayer.seekTo(index, 0)
            }
        }

        scope.launch {
            queueManager.currentTrack.collect { track ->
                val index = queueManager.queue.value.indexOfFirst { it.id == track?.id }
                if (index >= 0) {
                    exoPlayer.seekTo(index, 0)
                }
                _currentTrack.value = track
            }
        }
    }

    fun setQueue(tracks: List<Track>, startFrom: Long) {
        queueManager.setQueue(tracks)

        val index = if (startFrom != -1L)
            queueManager.queue.value.indexOfFirst { it.id == startFrom } else 0
        queueManager.setCurrentTrack(index)
    }

    fun setAlbumQueue(tracks: List<Track>) {
        queueManager.setQueue(tracks)
    }

    fun addToQueue(track: Track) = queueManager.addToQueue(track)

    fun play() {
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun pause() = exoPlayer.pause()
    fun togglePlayPause() = if (exoPlayer.isPlaying) pause() else play()
    fun seekTo(position: Long) = exoPlayer.seekTo(position)
    fun playNext() = queueManager.skipToNext()
    fun playPrevious() = queueManager.skipToPrevious()
    fun toggleLoop() = queueManager.toggleLoop()
    fun toggleShuffle() = queueManager.toggleShuffle()
    fun setSpeed(speed: Float) = exoPlayer.apply {
        setPlaybackSpeed(speed)
    }

    fun release() = exoPlayer.release()
}
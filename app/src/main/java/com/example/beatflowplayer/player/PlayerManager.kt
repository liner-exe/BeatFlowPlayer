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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class PlayerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
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
        })

        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (!isPlaying && exoPlayer.playWhenReady) return
                _isPlaying.value = isPlaying
            }
        })

        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _currentTrack.value = mediaItem?.toTrack()
                _position.value = 0
            }
        })

        exoPlayer.addListener(object : Player.Listener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                val updatedTracks = (0 until exoPlayer.mediaItemCount).map { index ->
                    exoPlayer.getMediaItemAt(index).toTrack()
                }
                _tracks.value = updatedTracks

                Log.d("player_manager", "updated")
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
    }

    fun setQueue(tracks: List<MediaItem>, index: Int) {
        _tracks.value = tracks.map { it.toTrack() }
        exoPlayer.setMediaItems(tracks, index, 0L)
    }

    fun addToQueue(track: Track) = exoPlayer.addMediaItem(track.toMediaItem())

    fun play() {
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun pause() = exoPlayer.pause()
    fun togglePlayPause() = if (exoPlayer.isPlaying) pause() else play()
    fun seekTo(position: Long) = exoPlayer.seekTo(position)
    fun playNext() = exoPlayer.seekToNextMediaItem()
    fun playPrevious() = exoPlayer.seekToPreviousMediaItem()

    fun toggleLoop() {
        exoPlayer.repeatMode = if (exoPlayer.repeatMode == Player.REPEAT_MODE_ONE)
            Player.REPEAT_MODE_OFF else Player.REPEAT_MODE_ONE
    }

    fun toggleShuffle() {
        exoPlayer.shuffleModeEnabled = !exoPlayer.shuffleModeEnabled
    }

    fun release() = exoPlayer.release()
}
package com.example.beatflowplayer.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.beatflowplayer.domain.model.Playlist
import com.example.beatflowplayer.domain.repository.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val audioRepository: AudioRepository
): ViewModel() {
    private val _playlists = mutableStateOf<List<Playlist>?>(emptyList())
    val playlists: List<Playlist>? get() = _playlists.value

    private val _isPlaylistsLoading = mutableStateOf(false)
    val isPlaylistsLoading: State<Boolean> = _isPlaylistsLoading

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            _isPlaylistsLoading.value = true
            try {
                _playlists.value = audioRepository.getAllPlaylists()
            } catch (e: Error) {

            } finally {
                _isPlaylistsLoading.value = false
            }
        }
    }
}
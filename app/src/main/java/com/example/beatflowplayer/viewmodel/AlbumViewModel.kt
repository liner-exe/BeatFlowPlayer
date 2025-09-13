package com.example.beatflowplayer.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.repository.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val audioRepository: AudioRepository
) : ViewModel() {
    private val _albums = mutableStateOf<List<Album>>(emptyList())
    val albums: State<List<Album>> = _albums

    private val _album = mutableStateOf<Album?>(null)
    val album: State<Album?> = _album

    private val _isAlbumsLoading = mutableStateOf<Boolean>(false)
    val isAlbumsLoading: State<Boolean> = _isAlbumsLoading

    fun loadAlbums() {
        _isAlbumsLoading.value = true

        viewModelScope.launch {
            _albums.value = audioRepository.getAllAlbums()
            _isAlbumsLoading.value = false
        }
    }

    fun loadAlbumById(id: Long) {
        viewModelScope.launch {
            _album.value = audioRepository.getAlbumById(id)
        }
    }

    fun togglePin(album: Album) {
        viewModelScope.launch {
            _albums.value = _albums.value.map {
                if (it.id == album.id) it.copy(isPinned = !it.isPinned) else it
            }
        }
    }
}
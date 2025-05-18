package com.example.beatflowplayer.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beatflowplayer.domain.model.Artist
import com.example.beatflowplayer.domain.repository.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val audioRepository: AudioRepository
) : ViewModel() {
    private val _artists = mutableStateOf<List<Artist>>(emptyList())
    val artists: State<List<Artist>> = _artists

    private val _artist = mutableStateOf<Artist?>(null)
    val artist: State<Artist?> = _artist

    private val _isArtistsLoading = mutableStateOf<Boolean>(false)
    val isArtistsLoading: State<Boolean> = _isArtistsLoading

    fun loadArtists() {
        _isArtistsLoading.value = true

        viewModelScope.launch {
            _artists.value = audioRepository.getAllArtists()
            _isArtistsLoading.value = false
        }
    }

    fun loadArtistById(id: Long) {
        viewModelScope.launch {
            _artist.value = audioRepository.getArtistById(id)
        }
    }
}
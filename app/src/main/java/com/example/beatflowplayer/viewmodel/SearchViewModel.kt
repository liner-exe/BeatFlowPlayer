package com.example.beatflowplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beatflowplayer.domain.enums.SearchCategory
import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.model.Artist
import com.example.beatflowplayer.domain.model.Track
import com.example.beatflowplayer.domain.repository.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val audioRepository: AudioRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedCategories = MutableStateFlow<Set<SearchCategory>>(
        setOf(SearchCategory.TRACKS, SearchCategory.ALBUMS, SearchCategory.ARTISTS)
    )
    val selectedCategories: StateFlow<Set<SearchCategory>> = _selectedCategories.asStateFlow()

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> = _artists

    init {
        viewModelScope.launch {
            combine(
                _query, _selectedCategories.map { it.toSortedSet() }
            ) { query, sortedCategories -> query to sortedCategories }
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { (query, categories) ->
                    search(query, categories)
                }
        }
    }

    fun updateQuery(newQuery: String) {
        _query.update { newQuery }

        if (newQuery.isBlank()) {
            _tracks.value = emptyList()
            _albums.value = emptyList()
            _artists.value = emptyList()
        }
    }

    fun toggleSelectedCategory(category: SearchCategory) {
        _selectedCategories.update {
            it.toMutableSet().apply {
                if (contains(category)) remove(category) else add(category)
            }
        }
    }

    private fun search(query: String, categories: Set<SearchCategory>) {
        viewModelScope.launch {
            if (_query.value.isBlank()) return@launch

            _tracks.update {
                if (SearchCategory.TRACKS in categories) {
                    audioRepository.searchTracks(query)
                } else {
                    emptyList()
                }
            }

            _albums.update {
                if (SearchCategory.ALBUMS in categories) {
                    audioRepository.searchAlbums(query)
                } else {
                    emptyList()
                }
            }

            _artists.update {
                if (SearchCategory.ARTISTS in categories) {
                    audioRepository.searchArtists(query)
                } else {
                    emptyList()
                }
            }
        }
    }
}

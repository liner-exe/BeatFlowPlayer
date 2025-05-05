package com.example.beatflowplayer.domain

import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.model.Track

interface AudioLocalDataSource {
    fun getAllTracks(): List<Track>

    fun getAllAlbums(): List<Album>
}
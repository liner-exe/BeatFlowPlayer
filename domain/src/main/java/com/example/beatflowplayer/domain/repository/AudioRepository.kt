package com.example.beatflowplayer.domain.repository

import com.example.beatflowplayer.domain.model.Track

interface AudioRepository {
    suspend fun getAllTracks(): List<Track>
}
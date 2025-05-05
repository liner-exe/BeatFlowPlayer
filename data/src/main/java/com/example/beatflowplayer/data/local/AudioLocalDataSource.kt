package com.example.beatflowplayer.data.local

import com.example.beatflowplayer.domain.AudioLocalDataSource
import com.example.beatflowplayer.domain.model.Album
import com.example.beatflowplayer.domain.model.Track
import javax.inject.Inject

class AudioLocalDataSourceImpl @Inject constructor() : AudioLocalDataSource {
    override fun getAllTracks(): List<Track> {
        TODO("Not yet implemented")
    }

    override fun getAllAlbums(): List<Album> {
        TODO("Not yet implemented")
    }
}
package com.example.beatflowplayer.domain.model.player

import com.example.beatflowplayer.domain.model.Track

data class QueueContext(
    val tracks: List<Track>,
    val source: SourceType
)
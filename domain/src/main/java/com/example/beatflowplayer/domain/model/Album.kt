package com.example.beatflowplayer.domain.model

data class Album(
    val id: Long,
    val title: String,
    val artists: List<String>,
    val artworkUri: String,
    val tracks: List<Track>
) {
    override fun equals(other: Any?): Boolean = other is Album && id == other.id
    override fun hashCode(): Int = id.hashCode()

    fun firstTrack(): Track = tracks.first()

    companion object {
        val empty = Album(
            -1,
            "",
            emptyList(),
            "",
            emptyList()
        )
    }
}
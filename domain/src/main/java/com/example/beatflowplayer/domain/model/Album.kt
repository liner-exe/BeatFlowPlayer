package com.example.beatflowplayer.domain.model

data class Album(
    val id: Long,
    val title: String,
    val artist: String,
    val artistId: Long,
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
            "",
            -1,
            "",
            emptyList()
        )
    }
}
package com.example.beatflowplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun TrackCard(track: Track) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = track.id.toString(),
                modifier = Modifier.align(alignment = Alignment.CenterVertically))

            Spacer(modifier = Modifier.width(20.dp))

            Image(painterResource(id = R.drawable.nominalo),
                contentDescription = "Cover",
                modifier = Modifier.size(50.dp))

            Spacer(modifier = Modifier.width(20.dp))

            Column(verticalArrangement = Arrangement.SpaceAround) {
                Text(text = track.title)
                Text(text = track.artist)
            }
        }
    }
}

@Composable
fun TrackList(tracks: List<Track>) {
    LazyColumn {
        items(tracks) { track ->
            TrackCard(track = track)
        }
    }
}
package com.example.beatflowplayer.ui.screens.album_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.beatflowplayer.R
import com.example.beatflowplayer.ui.navigation.Screen

@Composable
fun AlbumScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "BackButton")
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { }
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "EditButton")
            }

            IconButton(
                onClick = { }
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "MoreButton")
            }
        }

        Image(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp)),
            painter = painterResource(R.drawable.nominalo),
            contentDescription = "PlaylistCover"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 32.dp)
        ) {
            Text(
                text = "Album Name",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Album Author",
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = "PlayIcon"
                )

                Spacer(modifier = Modifier.size(5.dp))

                Text(
                    text = "Play",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))

            Button(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.shuffle),
                    contentDescription = "PlayIcon"
                )

                Spacer(modifier = Modifier.size(5.dp))

                Text(
                    text = "Shuffle",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
fun AlbumScreenPreview() {
    AlbumScreen(navController = rememberNavController())
}
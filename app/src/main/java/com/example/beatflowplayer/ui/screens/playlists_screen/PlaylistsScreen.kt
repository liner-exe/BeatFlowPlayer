package com.example.beatflowplayer.ui.screens.playlists_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun PlaylistsScreen(
    isSelected: Boolean,
    navController: NavHostController
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "You don't have any playlist.")
        }

        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn(),
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            ElevatedButton(
                onClick = {},
                modifier = Modifier
                    .size(96.dp)
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "AddIcon"
                )
            }
        }
    }
}

@Preview
@Composable
fun PlaylistsScreenPreview() {
    PlaylistsScreen(isSelected = false, navController = rememberNavController())
}
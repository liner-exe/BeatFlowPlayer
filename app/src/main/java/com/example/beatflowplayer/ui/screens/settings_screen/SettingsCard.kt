package com.example.beatflowplayer.ui.screens.settings_screen

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SettingsCardItem(
    val title: String,
    val description: String,
    val imageVector: ImageVector,
    val iconColor: Color
)

@Composable
fun SettingsCard(
    title: String,
    description: String,
    imageVector: ImageVector,
    iconColor: Color = Color.White
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        onClick = {},
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(48.dp),
                imageVector = imageVector,
                tint = iconColor,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingsCardPreview() {
    SettingsCard(
        title = "Test title",
        description = "This is test description and it is very very long",
        imageVector = Icons.Filled.AccountCircle
    )
}
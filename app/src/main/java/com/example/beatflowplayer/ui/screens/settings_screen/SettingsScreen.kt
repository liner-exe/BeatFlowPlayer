package com.example.beatflowplayer.ui.screens.settings_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.beatflowplayer.ui.navigation.Screen

@Composable
fun SettingsScreen(
    navController: NavHostController?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondaryContainer),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController?.popBackStack()
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = "Settings",
                fontSize = 16.sp
            )
        }

        val settingsTabs = listOf(
            SettingsCardItem(
                "Персонализация",
                "Здесь вы можете настроить приложение по своему вкусу",
                Icons.Filled.AccountCircle
            ),
            SettingsCardItem(
                "Звук",
                "Здесь вы можете настроить звук",
                Icons.Filled.Build
            ),
        )

        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            settingsTabs.forEach {
                SettingsCard(
                    title = it.title,
                    description = it.description,
                    imageVector = it.imageVector
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}
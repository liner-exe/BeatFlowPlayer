package com.example.beatflowplayer.ui.screens.player_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.beatflowplayer.R
import com.example.beatflowplayer.ui.navigation.Screen

@Composable
fun DrawerMenu(
    navController: NavHostController?
) {
    val drawerItems = listOf(
        DrawerItem(icon = Icons.Filled.Info, title = "Test Name"),
        DrawerItem(icon = Icons.Filled.Info, title = "Test Name"),
        DrawerItem(icon = Icons.Filled.Info, title = "Test Name"),
        DrawerItem(icon = Icons.Filled.Info, title = "Test Name"),
    )

    val drawerItemsBottom = listOf(
        DrawerItem(icon = Icons.Filled.Settings, title = "Settings")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .size(128.dp),
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "App icon"
                )

                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = "BeatFlow Player",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        drawerItems.forEach {
            NavigationDrawerItem(
                label = { Text(text = it.title) },
                icon = { Icon(it.icon, contentDescription = it.title) },
                onClick = {},
                selected = false,
                shape = RectangleShape,
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }

        HorizontalDivider(thickness = 2.dp)

        drawerItemsBottom.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = { Text(text = item.title) },
                icon = { Icon(item.icon, contentDescription = item.title) },
                onClick = {
                    when(index) {
                        0 -> navController?.navigate(Screen.SettingsScreen.route)
                        else -> {}
                    }
                },
                selected = false,
                shape = RectangleShape,
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}

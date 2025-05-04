package com.example.beatflowplayer.ui.screens.player_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.beatflowplayer.ui.navigation.Screen
import com.example.beatflowplayer.ui.theme.BeatFlowPlayerTheme
import com.example.beatflowplayer.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

data class RowItem(
    val title: String,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerUI(
    navController: NavHostController
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BackHandler(
        enabled = drawerState.isOpen
    ) {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerMenu(navController = navController)
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    title = {
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    append("BeatFlow")
                                }
                                append(" ")
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                ) {
                                    append("Player")
                                }
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                            Icon(Icons.Filled.Menu, contentDescription = "OpenMenu")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.SearchScreen.route)
                            }
                        ) {
                            Icon(
                                Icons.Filled.Search,
                                tint = MaterialTheme.colorScheme.onBackground,
                                contentDescription = "SearchButton"
                            )
                        }
                    })
            }
        ) { padding ->
            TabsPager(
                navController = navController,
                drawerState = drawerState,
                padding = padding
            )
        }
    }
}

@Preview
@Composable
fun PreviewAudioPlayerUI() {
    BeatFlowPlayerTheme {
        AudioPlayerUI(navController = rememberNavController())
    }
}
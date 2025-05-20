package com.example.beatflowplayer.ui.screens.player_screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.beatflowplayer.domain.navigation.Screen
import com.example.beatflowplayer.ui.screens.albums_screen.AlbumsScreen
import com.example.beatflowplayer.ui.screens.artists_screen.ArtistsScreen
import com.example.beatflowplayer.ui.screens.playlists_screen.PlaylistsScreen
import com.example.beatflowplayer.ui.screens.tracks_screen.TracksScreen
import com.example.beatflowplayer.viewmodel.PlayerViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("NewApi")
@Composable
fun TabsPager(
    navController: NavHostController,
    drawerState: DrawerState,
    playerViewModel: PlayerViewModel,
    padding: PaddingValues
) {
    val tabItems = listOf(
        RowItem(title = "Tracks", route = Screen.TracksScreen.route),
        RowItem(title = "Playlists", route = Screen.PlaylistsScreen.route),
        RowItem(title = "Albums", route = Screen.AlbumsScreen.route),
        RowItem(title = "Artists", route = Screen.ArtistsScreen.route)
    )

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { tabItems.size }

    val navigationHistory = remember { mutableStateListOf(0) }

    val context = LocalContext.current

    val track by playerViewModel.currentTrack

    LaunchedEffect(pagerState.currentPage) {
//        if (navigationHistory.lastOrNull() != pagerState.currentPage) {
//            navigationHistory.add(pagerState.currentPage)
//        }
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                snapshotFlow { pagerState.isScrollInProgress }
                    .filter { inProgress -> !inProgress }
                    .first()

                if (navigationHistory.lastOrNull() != page) {
                    navigationHistory.add(page)
                }
            }
    }

    BackHandler(enabled = navigationHistory.size > 1 && drawerState.isClosed) {
        scope.launch {
            navigationHistory.removeLast()
            val previousTab = navigationHistory.last()
            pagerState.animateScrollToPage(previousTab)
        }
    }

    Column(
        modifier = Modifier
            .padding(padding)
    ) {
        TabRow(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier
                .fillMaxWidth(),
            tabs = {
                tabItems.forEachIndexed { index, item ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = item.title) })
                }
            })

        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (track != null) 96.dp else 0.dp)
            ) { index ->
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    when (index) {
                        0 -> TracksScreen(pagerState.currentPage == 0, playerViewModel)
                        1 -> PlaylistsScreen(
                            pagerState.currentPage == 1,
                            navController = navController
                        )
                        2 -> AlbumsScreen(
                            pagerState.currentPage == 2,
                            navController = navController
                        )
                        else -> ArtistsScreen(
                            pagerState.currentPage == 3,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
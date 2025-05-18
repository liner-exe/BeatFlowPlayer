package com.example.beatflowplayer.ui.widget

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.beatflowplayer.ui.screens.player_screen.SheetCollapsed
import com.example.beatflowplayer.ui.screens.player_screen.SheetCollapsedContent
import com.example.beatflowplayer.ui.screens.player_screen.SheetExpanded
import com.example.beatflowplayer.ui.screens.player_screen.SheetExpandedContent
import com.example.beatflowplayer.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetOverlay(
    navController: NavHostController,
    playerViewModel: PlayerViewModel
) {
    val track by playerViewModel.currentTrack

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden && track != null
        }
    )

    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)
    var fullHeight by remember { mutableStateOf(0.dp) }
    val peekHeight = remember { 96.dp }
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val progress by remember {
        derivedStateOf {
            try {
                val currentOffset = sheetState.requireOffset()
                val fullHeightPx = with(density) { fullHeight.toPx() }
                val peekHeightPx = with(density) { peekHeight.toPx() }

                when {
                    fullHeightPx <= peekHeightPx -> 1f
                    else -> (fullHeightPx - currentOffset - peekHeightPx) / (fullHeightPx - peekHeightPx)
                }.coerceIn(0f, 1f)
            } catch (e: Exception) {
                0f
            }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            Log.d("overlay", track?.title.toString() + track?.artist.toString())
            delay(300)
        }
    }

    BackHandler(
        enabled = progress == 1f
    ) {
        scope.launch {
            sheetState.partialExpand()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = if (track != null) peekHeight else 0.dp,
        sheetDragHandle = null,
        sheetShape = RectangleShape,
        sheetContainerColor = Color.Black,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { size ->
                        fullHeight = with(density) {
                            size.height.toDp()
                        }
                    }
            ) {
                SheetExpanded(
                    progress = progress,
                    content = {
                        SheetExpandedContent(
                            progress = progress,
                            scaffoldState = scaffoldState,
                            navController = navController,
                            playerViewModel = playerViewModel
                        )
                    }
                )

                SheetCollapsed {
                    SheetCollapsedContent(
                        progress = progress,
                        scaffoldState = scaffoldState
                    )
                }
            }
        }
    ) {

    }
}
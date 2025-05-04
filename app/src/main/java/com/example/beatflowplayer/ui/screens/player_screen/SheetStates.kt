package com.example.beatflowplayer.ui.screens.player_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun SheetExpanded(
    content: @Composable BoxScope.() -> Unit,
    progress: Float
) {
    Box(
        modifier = Modifier.zIndex(if (progress > 0.5f) 1f else 0f)
    ) {
        content()
    }
}

@Composable
fun SheetCollapsed(
    content: @Composable BoxScope.() -> Unit
) {
    Box {
        content()
    }

}
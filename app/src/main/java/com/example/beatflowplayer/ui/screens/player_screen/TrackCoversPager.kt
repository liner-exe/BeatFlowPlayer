package com.example.beatflowplayer.ui.screens.player_screen

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.beatflowplayer.R
import com.example.beatflowplayer.utils.getAlbumCover
import com.example.beatflowplayer.viewmodel.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun TrackCoversPager(
    playerViewModel: PlayerViewModel,
    context: Context,
    modifier: Modifier = Modifier
) {
    val queue by playerViewModel.trackQueue.collectAsState()
    val currentIndex by playerViewModel.currentIndex
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 })

    val scope = rememberCoroutineScope()
    val covers = remember { mutableStateListOf<Bitmap?>(null, null, null) }

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(currentIndex, queue) {
        isLoading = true
        val indices = listOf(currentIndex - 1, currentIndex, currentIndex + 1)
        indices.forEachIndexed { i, queueIndex ->
            val track = queue.getOrNull(queueIndex)
            if (track != null) {
                val bitmap = withContext(Dispatchers.IO) { getAlbumCover(context, track.uri) }
                if (covers.size > i) covers[i] = bitmap else covers.add(bitmap)
            } else {
                if (covers.size > i) covers[i] = null else covers.add(null)
            }
        }
        isLoading = false
    }

    var isTransitioning by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.settledPage) {
        if (pagerState.settledPage == 0 || pagerState.settledPage == 2) {
            isTransitioning = true

            val next = pagerState.settledPage == 2
            val newIndex = if (next) currentIndex + 1 else currentIndex - 1
            val indices = listOf(newIndex - 1, newIndex, newIndex + 1)

            indices.forEachIndexed { i, idx ->
                val track = queue.getOrNull(idx)
                val bitmap = if (track != null) {
                    withContext(Dispatchers.IO) { getAlbumCover(context, track.uri) }
                } else null

                if (covers.size > i) covers[i] = bitmap else covers.add(bitmap)
            }

            if (next) playerViewModel.playNext() else playerViewModel.playPrevious()

            pagerState.scrollToPage(1)

            isTransitioning = false
        }
    }

    Box(
        modifier = modifier
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(shape = RoundedCornerShape(12.dp)),
        ) { page ->
            val image = covers.getOrNull(page)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(targetState = image, animationSpec = tween(400)) { img ->
                    if (img != null) {
                        Image(
                            bitmap = img.asImageBitmap(),
                            contentDescription = "Track Cover",
                            modifier = Modifier
                                .size(300.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.default_cover),
                            contentDescription = "Default Cover",
                            modifier = Modifier
                                .size(300.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }
        }
    }
}
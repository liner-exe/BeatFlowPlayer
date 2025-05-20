package com.example.beatflowplayer.ui.widget.queue

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SoundWaveIndicator(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    barCount: Int = 3,
    color: Color = MaterialTheme.colorScheme.primary,
    animationDuration: Int = 500
) {
    val lastHeights = remember { mutableStateListOf(*Array(barCount) { 0.2f }) }

    Row(
        modifier = modifier
            .height(24.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(barCount) { index ->
            val infiniteTransition = rememberInfiniteTransition()
            val delay = index * 100

            val infiniteHeight by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = animationDuration
                        0.2f at delay
                        1f at delay + 200
                        0.2f at delay + 400
                    },
                    repeatMode = RepeatMode.Reverse
                )
            )

            if (isPlaying) {
                lastHeights[index]
            }

            val targetHeight = if (isPlaying) infiniteHeight else 0.2f
            val animatedHeight by animateFloatAsState(
                targetValue = targetHeight,
                animationSpec = tween(500)
            )

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(10.dp * if (isPlaying) infiniteHeight else animatedHeight)
                    .background(color, RoundedCornerShape(2.dp))
                    .align(Alignment.CenterVertically)
            )
        }
    }
}
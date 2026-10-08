package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors

// Placeholder row shown while a feedback list loads.
@Composable
fun ModerateSkeletonRow(
    modifier: Modifier = Modifier,
    titleWidthFraction: Float,
    subtitleWidthFraction: Float
) {
    val alpha by rememberInfiniteTransition(label = "skeleton").animateFloat(
        initialValue = 1f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAlpha"
    )
    ModerateListRow(modifier = modifier) {
        Row(
            modifier = Modifier.alpha(alpha = alpha),
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
        ) {
            Column(modifier = Modifier.weight(weight = 1f)) {
                SkeletonBar(
                    modifier = Modifier
                        .fillMaxWidth(fraction = titleWidthFraction)
                        .height(height = 13.dp)
                )
                SkeletonBar(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(fraction = subtitleWidthFraction)
                        .height(height = 10.dp)
                )
            }
            SkeletonBar(modifier = Modifier.size(width = 34.dp, height = 20.dp))
        }
    }
}

@Composable
private fun SkeletonBar(modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(color = colors.parchment, shape = RoundedCornerShape(size = 6.dp)))
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ModerateSkeletonRow(
            modifier = Modifier.fillMaxWidth(),
            titleWidthFraction = 0.5f,
            subtitleWidthFraction = 0.35f
        )
    }
}

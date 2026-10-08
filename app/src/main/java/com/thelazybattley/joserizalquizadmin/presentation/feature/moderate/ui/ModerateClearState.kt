package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

// Shown when a feedback list has nothing left to review.
@Composable
fun ModerateClearState(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    @StringRes bodyRes: Int
) {
    val borderColor = colors.taupe
    Column(
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = CornerRadius(x = 16.dp.toPx(), y = 16.dp.toPx()),
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(6.dp.toPx(), 4.dp.toPx()))
                    )
                )
            }
            .padding(horizontal = 22.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size = 52.dp)
                .background(color = colors.softSage, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Rounded.Check, contentDescription = null, tint = colors.deepMoss)
        }
        Text(
            text = stringResource(id = titleRes),
            style = typography.semiBold18,
            color = colors.espresso,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = bodyRes),
            style = typography.regular13.copy(lineHeight = 20.sp),
            color = colors.woodsmokeBrown,
            textAlign = TextAlign.Center
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ModerateClearState(
            modifier = Modifier.fillMaxWidth(),
            titleRes = R.string.no_reported_questions_title,
            bodyRes = R.string.no_reported_questions
        )
    }
}

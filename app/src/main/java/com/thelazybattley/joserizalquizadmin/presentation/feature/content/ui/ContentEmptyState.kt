package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
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

// Shown when Debug has no books at all.
@Composable
fun ContentEmptyState(
    modifier: Modifier = Modifier,
    onAddBook: () -> Unit
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
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size = 52.dp)
                .background(color = colors.antiqueCream, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                contentDescription = null,
                tint = colors.antiqueGold
            )
        }
        Text(
            text = stringResource(id = R.string.start_the_library),
            style = typography.semiBold18,
            color = colors.espresso
        )
        Text(
            text = stringResource(id = R.string.start_the_library_body),
            style = typography.regular13.copy(lineHeight = 20.sp),
            color = colors.woodsmokeBrown,
            textAlign = TextAlign.Center
        )
        ContentAddBookButton(
            modifier = Modifier.padding(top = 6.dp),
            text = stringResource(id = R.string.add_a_book),
            onClick = onAddBook
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ContentEmptyState(
            modifier = Modifier
                .background(color = colors.warmLinen, shape = RoundedCornerShape(size = 16.dp))
                .fillMaxWidth(),
            onAddBook = {}
        )
    }
}

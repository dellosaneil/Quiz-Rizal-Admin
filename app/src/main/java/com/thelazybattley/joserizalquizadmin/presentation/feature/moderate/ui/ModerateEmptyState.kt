package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@Composable
fun ModerateEmptyState(
    modifier: Modifier = Modifier,
    glyph: String,
    @StringRes messageRes: Int
) {
    val borderColor = APP_BORDER_COLOR
    Column(
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = CornerRadius(x = 14.dp.toPx(), y = 14.dp.toPx()),
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(4.dp.toPx(), 3.dp.toPx())
                        )
                    )
                )
            }
            .padding(start = 16.dp, top = 22.dp, end = 16.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = glyph,
            style = typography.regular18.copy(fontSize = 19.sp),
            color = colors.taupe
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .widthIn(max = 220.dp),
            text = stringResource(id = messageRes),
            style = typography.regular11,
            color = colors.taupe,
            textAlign = TextAlign.Center
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ModerateEmptyState(
            modifier = Modifier.fillMaxWidth(),
            glyph = "◆",
            messageRes = R.string.no_suggested_books
        )
    }
}

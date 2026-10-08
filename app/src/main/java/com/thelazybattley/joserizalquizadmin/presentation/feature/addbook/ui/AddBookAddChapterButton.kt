package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun AddBookAddChapterButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val borderColor = colors.taupe
    Row(
        modifier = modifier
            .height(height = 48.dp)
            .clip(shape = RoundedCornerShape(size = 14.dp))
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawRoundRect(
                    color = borderColor,
                    topLeft = Offset(x = strokeWidth / 2, y = strokeWidth / 2),
                    size = size.copy(width = size.width - strokeWidth, height = size.height - strokeWidth),
                    cornerRadius = CornerRadius(x = 14.dp.toPx(), y = 14.dp.toPx()),
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(6.dp.toPx(), 4.dp.toPx()))
                    )
                )
            }
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(space = 6.dp, alignment = Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            tint = colors.maroon,
            modifier = Modifier.size(size = 18.dp)
        )
        Text(
            text = stringResource(id = R.string.add_chapter),
            style = typography.bold14,
            color = colors.maroon
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddBookAddChapterButton(modifier = Modifier.fillMaxWidth()) {}
    }
}

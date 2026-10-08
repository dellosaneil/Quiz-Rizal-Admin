package com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

// Small uppercase pill used for build status and change kinds.
@Composable
fun ReleaseTag(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Text(
        modifier = modifier
            .background(color = containerColor, shape = RoundedCornerShape(size = 20.dp))
            .padding(horizontal = 9.dp, vertical = 4.dp),
        text = text,
        style = typography.semiBold10.copy(fontFamily = FontFamily.Monospace),
        color = contentColor
    )
}

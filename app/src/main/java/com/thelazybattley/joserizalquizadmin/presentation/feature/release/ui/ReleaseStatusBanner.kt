package com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

enum class ReleaseBannerType {
    SUCCESS,
    ERROR
}

@Composable
fun ReleaseStatusBanner(
    modifier: Modifier = Modifier,
    type: ReleaseBannerType,
    title: String,
    message: String
) {
    val (containerColor: Color, accentColor: Color, icon: ImageVector) = when (type) {
        ReleaseBannerType.SUCCESS -> Triple(colors.softSage, colors.deepMoss, Icons.Rounded.Check)
        ReleaseBannerType.ERROR -> Triple(colors.softBlush, colors.brickRed, Icons.Rounded.PriorityHigh)
    }
    Row(
        modifier = modifier
            .background(color = containerColor, shape = RoundedCornerShape(size = 16.dp))
            .padding(all = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size = 32.dp)
                .background(color = accentColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = containerColor,
                modifier = Modifier.size(size = 18.dp)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(space = 2.dp)) {
            Text(text = title, style = typography.bold14, color = accentColor)
            Text(text = message, style = typography.regular12, color = accentColor)
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ReleaseStatusBanner(
            modifier = Modifier.fillMaxWidth(),
            type = ReleaseBannerType.SUCCESS,
            title = "Release is up to date",
            message = "All 5 changes are live."
        )
    }
}

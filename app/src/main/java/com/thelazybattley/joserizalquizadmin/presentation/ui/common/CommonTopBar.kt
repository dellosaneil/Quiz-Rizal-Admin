package com.thelazybattley.joserizalquizadmin.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_PADDING

@Composable
fun CommonTopBar(
    modifier: Modifier = Modifier,
    onIconClicked: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = APP_PADDING
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        if (onIconClicked != null) {
            Box(
                modifier = Modifier
                    .border(width = 1.dp, color = colors.taupe, shape = CircleShape)
                    .clip(shape = CircleShape)
                    .clickable { onIconClicked() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_left_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .size(size = 12.dp),
                    tint = colors.espresso
                )
            }
        }
        content(Modifier.weight(weight = 1f))
        actions()
    }
}

@PreviewLightDark
@Composable
private fun PreviewCommonTopBar() {
    AppTheme {
        CommonTopBar(
            modifier = Modifier.background(color = colors.warmIvory),
            onIconClicked = {}
        ) {

        }
    }
}

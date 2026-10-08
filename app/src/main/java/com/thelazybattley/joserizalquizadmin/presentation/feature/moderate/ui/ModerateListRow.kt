package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

// Card shell shared by suggestion, report and skeleton rows.
@Composable
fun ModerateListRow(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .background(color = colors.ivoryMist, shape = RoundedCornerShape(size = 16.dp))
            .border(width = 1.dp, color = APP_BORDER_COLOR, shape = RoundedCornerShape(size = 16.dp))
            .padding(all = 14.dp),
        content = content
    )
}

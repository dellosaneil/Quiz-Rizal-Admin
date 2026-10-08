package com.thelazybattley.joserizalquizadmin.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

// Pill with a label and a count; place several in a Row with Modifier.selectableGroup().
@Composable
fun CommonFilterChip(
    modifier: Modifier = Modifier,
    label: String,
    count: Int?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(size = 18.dp)
    Row(
        modifier = modifier
            .height(height = 36.dp)
            .clip(shape = shape)
            .background(color = if (isSelected) colors.antiqueCream else colors.ivoryMist, shape = shape)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) colors.antiqueGold else APP_BORDER_COLOR,
                shape = shape
            )
            .selectable(selected = isSelected, role = Role.RadioButton, onClick = onClick)
            .padding(start = 14.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Text(
            text = label,
            style = typography.semiBold13,
            color = colors.espresso
        )
        // Null while the count is still loading.
        if (count != null) {
            Text(
                text = count.toString(),
                style = typography.regular11.copy(fontFamily = FontFamily.Monospace),
                color = if (isSelected) colors.antiqueGold else colors.woodsmokeBrown
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        CommonFilterChip(label = "Novel", count = 2, isSelected = true) {}
    }
}

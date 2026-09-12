package com.thelazybattley.joserizalquizadmin.presentation.ui.common

import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors

@Composable
fun CommonRadioButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    RadioButton(
        modifier = modifier,
        selected = isSelected,
        onClick = onClick,
        colors = RadioButtonDefaults.colors(
            selectedColor = colors.deepMoss
        )
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        CommonRadioButton(
            modifier = Modifier,
            isSelected = true,
            onClick = {}
        )
    }
}

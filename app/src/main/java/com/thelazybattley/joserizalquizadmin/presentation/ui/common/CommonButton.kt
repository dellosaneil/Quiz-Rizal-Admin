package com.thelazybattley.joserizalquizadmin.presentation.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = colors.maroon,
        contentColor = colors.white,
        disabledContentColor = colors.white,
        disabledContainerColor = colors.maroon.copy(
            alpha = 0.45f
        )
    ),
    border: BorderStroke? = null,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = buttonColors,
        enabled = enabled,
        shape = RoundedCornerShape(size = 8.dp),
        border = border
    ) {
        Text(
            text = text,
            style = typography.bold14
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        CommonButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Enabled Button"
        ) { }
    }
}

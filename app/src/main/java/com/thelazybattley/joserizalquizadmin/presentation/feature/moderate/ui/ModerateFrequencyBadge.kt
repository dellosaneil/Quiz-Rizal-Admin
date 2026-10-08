package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.annotation.PluralsRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun ModerateFrequencyBadge(
    modifier: Modifier = Modifier,
    count: Int,
    @PluralsRes labelRes: Int
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = stringResource(id = R.string.value_x, count),
            style = typography.semiBold14.copy(fontFamily = FontFamily.Monospace),
            color = colors.antiqueGold
        )
        Text(
            text = pluralStringResource(id = labelRes, count = count),
            style = typography.semiBold10.copy(fontFamily = FontFamily.Monospace),
            color = colors.taupe
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ModerateFrequencyBadge(count = 34, labelRes = R.plurals.students)
    }
}

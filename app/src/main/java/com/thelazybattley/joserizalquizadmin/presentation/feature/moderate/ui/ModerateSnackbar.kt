package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

// Built on a flat Surface: Material's Snackbar always draws an elevation shadow,
// which shows as a faint ring around a light snackbar on a light screen.
@Composable
fun ModerateSnackbar(
    modifier: Modifier = Modifier,
    snackbarData: SnackbarData
) {
    Surface(
        modifier = modifier
            .padding(all = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(size = 12.dp),
        color = colors.ivoryMist,
        contentColor = colors.espresso
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 52.dp)
                .padding(start = 16.dp, end = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(weight = 1f),
                text = snackbarData.visuals.message,
                style = typography.regular13,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            snackbarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(onClick = { snackbarData.performAction() }) {
                    Text(
                        text = actionLabel,
                        style = typography.bold12,
                        color = colors.maroon
                    )
                }
            }
        }
    }
}

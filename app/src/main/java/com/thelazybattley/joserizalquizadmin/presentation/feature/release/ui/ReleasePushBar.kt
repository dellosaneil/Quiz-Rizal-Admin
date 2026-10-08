package com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ReleaseActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ReleaseCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun ReleasePushBar(
    modifier: Modifier = Modifier,
    changeCount: Int,
    bookCount: Int,
    isPushing: Boolean,
    callback: ReleaseCallback
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(height = 52.dp),
        onClick = { callback.handleAction(action = ReleaseActions.RequestPush) },
        enabled = !isPushing,
        shape = RoundedCornerShape(size = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.maroon,
            contentColor = colors.white,
            disabledContainerColor = colors.maroon.copy(alpha = 0.7f),
            disabledContentColor = colors.white
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isPushing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(size = 16.dp),
                    color = colors.white,
                    trackColor = colors.white.copy(alpha = 0.35f),
                    strokeWidth = 2.dp
                )
                Text(
                    text = pluralStringResource(id = R.plurals.pushing_books, count = bookCount, bookCount),
                    style = typography.bold14
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.ArrowUpward,
                    contentDescription = null,
                    modifier = Modifier.size(size = 16.dp)
                )
                Text(
                    text = pluralStringResource(id = R.plurals.push_changes, count = changeCount, changeCount),
                    style = typography.bold14
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ReleasePushBar(changeCount = 5, bookCount = 3, isPushing = false, callback = ReleaseCallback.default())
    }
}

package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

// Shown when the title and author were pre-filled from a Moderate suggestion.
@Composable
fun AddBookSuggestionBanner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(color = colors.antiqueCream, shape = RoundedCornerShape(size = 14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_flag),
            contentDescription = null,
            tint = colors.antiqueGold,
            modifier = Modifier.size(size = 20.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(space = 2.dp)) {
            Text(
                text = stringResource(id = R.string.from_suggestion_title),
                style = typography.bold12.copy(fontSize = typography.semiBold13.fontSize),
                color = colors.espresso
            )
            Text(
                text = stringResource(id = R.string.from_suggestion_body),
                style = typography.regular12,
                color = colors.woodsmokeBrown
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddBookSuggestionBanner(modifier = Modifier.fillMaxWidth())
    }
}

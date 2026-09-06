package com.thelazybattley.joserizalquizadmin.presentation.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun HomeTabScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.all_time),
                style = typography.semiBold11,
                color = colors.taupe
            )

        }
        item {
            HomeStatisticsSection()
        }
        item {
            Text(
                text = stringResource(id = R.string.most_suggested),
                style = typography.semiBold11,
                color = colors.taupe
            )
            HomeFeedbackCard(
                modifier = Modifier.padding(top = 8.dp),
                count = 1,
                title = "The Title",
                label = R.plurals.students,
                subtitle = ""
            )

            HomeFeedbackCard(
                modifier = Modifier.padding(top = 8.dp),
                count = 2,
                title = "The Title 2",
                label = R.plurals.students,
                subtitle = ""
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.most_reported),
                style = typography.semiBold11,
                color = colors.taupe
            )
            HomeFeedbackCard(
                modifier = Modifier.padding(top = 8.dp),
                count = 1,
                title = "The Title",
                label = R.plurals.students,
                subtitle = "Chapter 1"
            )

            HomeFeedbackCard(
                modifier = Modifier.padding(top = 8.dp),
                count = 2,
                title = "The Title 2",
                label = R.plurals.students,
                subtitle = "Chapter 2"
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewHomeTabScreen() {
    AppTheme {
        HomeTabScreen()
    }
}

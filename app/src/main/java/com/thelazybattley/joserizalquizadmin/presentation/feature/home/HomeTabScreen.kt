package com.thelazybattley.joserizalquizadmin.presentation.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
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
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun HomeTabScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        containerColor = APP_BACKGROUND
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues = innerPadding),
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

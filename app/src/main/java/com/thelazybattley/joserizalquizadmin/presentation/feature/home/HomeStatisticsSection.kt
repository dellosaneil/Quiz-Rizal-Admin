package com.thelazybattley.joserizalquizadmin.presentation.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_PADDING

@Composable
fun HomeStatisticsSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            StatisticsCard(
                modifier = Modifier.weight(weight = 1f),
                count = 2,
                title = stringResource(id = R.string.books_live, 2),
                subtitle = stringResource(id = R.string.questions, 2)
            )

            StatisticsCard(
                modifier = Modifier.weight(weight = 1f),
                count = 3,
                title = pluralStringResource(id = R.plurals.suggestions, 3),
                subtitle = stringResource(id = R.string.needs_review)
            )
        }
        StatisticsCard(
            count = 3,
            title = pluralStringResource(id = R.plurals.open_reports, 3),
            subtitle = stringResource(id = R.string.needs_review),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun StatisticsCard(
    modifier: Modifier = Modifier,
    count: Int,
    title: String,
    subtitle: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colors.ivoryMist
        ),
        shape = RoundedCornerShape(size = 8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = APP_BORDER_COLOR
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
            modifier = Modifier
                .padding(all = APP_PADDING),
        ) {
            Text(
                text = count.toString(),
                style = typography.bold23,
                color = colors.espresso
            )
            Text(
                text = title,
                style = typography.semiBold13,
                color = colors.taupe
            )
            Text(
                text = subtitle,
                style = typography.semiBold11,
                color = colors.antiqueGold
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun PreviewHomeStatisticsSection() {
    AppTheme {
        HomeStatisticsSection(
            modifier = Modifier.background(color = APP_BACKGROUND)
        )
    }
}

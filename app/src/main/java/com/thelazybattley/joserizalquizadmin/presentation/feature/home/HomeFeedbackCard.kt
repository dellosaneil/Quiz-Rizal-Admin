package com.thelazybattley.joserizalquizadmin.presentation.feature.home

import androidx.annotation.PluralsRes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_PADDING

@Composable
fun HomeFeedbackCard(
    modifier: Modifier = Modifier,
    count: Int,
    title: String,
    subtitle: String,
    @PluralsRes label: Int
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = APP_PADDING),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                modifier = Modifier.weight(weight = 1f)
            ) {
                Text(
                    text = title,
                    style = typography.semiBold14,
                    color = colors.espresso,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = typography.regular11,
                        color = colors.woodsmokeBrown,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.value_x, count),
                    style = typography.semiBold14,
                    color = colors.antiqueGold,
                    textAlign = TextAlign.End,
                )
                Text(
                    text = pluralStringResource(id = label, count, count),
                    style = typography.semiBold10,
                    color = colors.taupe
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewHomeFeedbackCard() {
    AppTheme {
        HomeFeedbackCard(
            modifier = Modifier.fillMaxWidth(),
            count = 2,
            title = "The title",
            subtitle = "The subtitle",
            label = R.plurals.students
        )

    }
}

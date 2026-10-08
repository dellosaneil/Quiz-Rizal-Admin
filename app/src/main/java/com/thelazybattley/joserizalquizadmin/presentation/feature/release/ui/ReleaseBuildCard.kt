package com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.release.BuildSummary
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateListRow
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun ReleaseBuildCard(
    modifier: Modifier = Modifier,
    name: String,
    tag: String,
    tagContainerColor: Color,
    tagContentColor: Color,
    summary: BuildSummary
) {
    ModerateListRow(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, style = typography.bold14, color = colors.espresso)
            ReleaseTag(text = tag, containerColor = tagContainerColor, contentColor = tagContentColor)
        }
        Row(
            modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 14.dp)
        ) {
            BuildCount(
                count = summary.bookCount,
                label = pluralStringResource(id = R.plurals.books_label, count = summary.bookCount)
            )
            BuildCount(
                count = summary.questionCount,
                label = pluralStringResource(id = R.plurals.questions_label, count = summary.questionCount)
            )
        }
    }
}

@Composable
private fun BuildCount(count: Int, label: String) {
    Column {
        Text(
            text = count.toString(),
            style = typography.bold23.copy(fontSize = 26.sp, lineHeight = 28.sp),
            color = colors.espresso
        )
        Text(text = label, style = typography.regular11, color = colors.woodsmokeBrown)
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ReleaseBuildCard(
            modifier = Modifier.fillMaxWidth(),
            name = "Release",
            tag = "LIVE",
            tagContainerColor = colors.softSage,
            tagContentColor = colors.deepMoss,
            summary = BuildSummary(bookCount = 2, questionCount = 11)
        )
    }
}

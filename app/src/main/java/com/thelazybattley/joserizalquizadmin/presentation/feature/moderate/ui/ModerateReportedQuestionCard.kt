package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseTag
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModerateReportedQuestionCard(
    modifier: Modifier = Modifier,
    reportedQuestion: RankedReportedQuestion,
    callbacks: ModerateCallback
) {
    ModerateListRow(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(weight = 1f),
                text = reportedQuestion.bookTitle?.let { title ->
                    stringResource(id = R.string.book_chapter, title, reportedQuestion.chapterNumber)
                } ?: stringResource(id = R.string.chapter_only, reportedQuestion.chapterNumber),
                style = typography.regular12,
                color = colors.woodsmokeBrown,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            ReleaseTag(
                text = pluralStringResource(
                    id = R.plurals.report_count_tag,
                    count = reportedQuestion.reportCount,
                    reportedQuestion.reportCount
                ),
                containerColor = colors.softBlush,
                contentColor = colors.brickRed
            )
        }
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = reportedQuestion.question,
            style = typography.semiBold16,
            color = colors.espresso
        )
        if (reportedQuestion.reasons.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
                verticalArrangement = Arrangement.spacedBy(space = 6.dp)
            ) {
                reportedQuestion.reasons.forEach { reason ->
                    Text(
                        modifier = Modifier
                            .background(color = colors.warmLinen, shape = RoundedCornerShape(size = 8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        text = reason,
                        style = typography.regular12,
                        color = colors.woodsmokeBrown
                    )
                }
            }
        }
        OutlinedButton(
            modifier = Modifier
                .padding(top = 12.dp)
                .height(height = 40.dp),
            onClick = {
                callbacks.handleAction(action = ModerateActions.DismissReport(reportedQuestion = reportedQuestion))
            },
            shape = RoundedCornerShape(size = 10.dp),
            border = BorderStroke(width = 1.dp, color = APP_BORDER_COLOR),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.espresso),
            contentPadding = PaddingValues(horizontal = 14.dp)
        ) {
            ModerateCardButtonContent(icon = null, text = stringResource(id = R.string.dismiss))
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ModerateReportedQuestionCard(
            modifier = Modifier.fillMaxWidth(),
            reportedQuestion = RankedReportedQuestion.dummy().copy(
                reasons = listOf("Answer key seems wrong", "Two answers fit")
            ),
            callbacks = ModerateCallback.default()
        )
    }
}

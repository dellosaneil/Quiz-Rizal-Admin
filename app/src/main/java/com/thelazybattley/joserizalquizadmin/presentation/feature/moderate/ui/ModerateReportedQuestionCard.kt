package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@Composable
fun ModerateReportedQuestionCard(
    modifier: Modifier = Modifier,
    reportedQuestion: RankedReportedQuestion
) {
    ModerateListRow(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(space = 10.dp)) {
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = stringResource(id = R.string.quoted_value, reportedQuestion.question),
                    style = typography.semiBold14,
                    color = colors.espresso,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.padding(top = 3.dp),
                    text = stringResource(
                        id = R.string.book_chapter,
                        reportedQuestion.bookTitle ?: stringResource(id = R.string.unknown_book),
                        reportedQuestion.chapterNumber
                    ),
                    style = typography.regular11,
                    color = colors.woodsmokeBrown
                )
                if (reportedQuestion.reasons.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(space = 6.dp)
                    ) {
                        reportedQuestion.reasons.forEach { reason ->
                            Text(
                                text = reason,
                                style = typography.regular11,
                                color = colors.woodsmokeBrown,
                                modifier = Modifier
                                    .background(color = colors.ivoryMist, shape = RoundedCornerShape(size = 20.dp))
                                    .border(width = 1.dp, color = APP_BORDER_COLOR, shape = RoundedCornerShape(size = 20.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
            ModerateFrequencyBadge(count = reportedQuestion.reportCount, labelRes = R.plurals.reports)
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ModerateReportedQuestionCard(
            modifier = Modifier.fillMaxWidth(),
            reportedQuestion = RankedReportedQuestion.dummy()
        )
    }
}

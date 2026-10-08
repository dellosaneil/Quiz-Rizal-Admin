package com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

// Why students flagged this question, shown at the top while fixing it.
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditQuestionReportBanner(
    modifier: Modifier = Modifier,
    report: RankedReportedQuestion
) {
    Column(
        modifier = modifier
            .background(color = colors.softBlush, shape = RoundedCornerShape(size = 14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_flag),
                contentDescription = null,
                tint = colors.brickRed,
                modifier = Modifier.size(size = 16.dp)
            )
            Text(
                text = pluralStringResource(id = R.plurals.students_reported_question, count = report.reportCount, report.reportCount),
                style = typography.bold12.copy(fontSize = 13.sp),
                color = colors.brickRed
            )
        }
        if (report.reasons.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
                verticalArrangement = Arrangement.spacedBy(space = 6.dp)
            ) {
                report.reasons.forEach { reason ->
                    Text(
                        modifier = Modifier
                            .background(color = colors.ivoryMist, shape = RoundedCornerShape(size = 8.dp))
                            .padding(horizontal = 9.dp, vertical = 4.dp),
                        text = reason,
                        style = typography.regular12,
                        color = colors.brickRed
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        EditQuestionReportBanner(
            modifier = Modifier.fillMaxWidth(),
            report = RankedReportedQuestion.dummy().copy(reasons = listOf("Answer key seems wrong", "Two answers fit"))
        )
    }
}

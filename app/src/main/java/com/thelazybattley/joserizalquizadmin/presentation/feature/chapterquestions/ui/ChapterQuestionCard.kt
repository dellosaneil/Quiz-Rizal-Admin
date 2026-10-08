package com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Question
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseTag
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@Composable
fun ChapterQuestionCard(
    modifier: Modifier = Modifier,
    number: Int,
    question: Question,
    reportCount: Int,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(size = 16.dp)
    Row(
        modifier = modifier
            .clip(shape = shape)
            .background(color = colors.ivoryMist, shape = shape)
            .border(width = 1.dp, color = APP_BORDER_COLOR, shape = shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(top = 2.dp)
                .width(width = 20.dp),
            text = number.toString(),
            style = typography.semiBold12.copy(fontFamily = FontFamily.Monospace),
            color = colors.antiqueGold
        )
        Column(
            modifier = Modifier.weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {
            Text(text = question.question, style = typography.semiBold14, color = colors.espresso)
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = colors.deepMoss,
                    modifier = Modifier.size(size = 14.dp)
                )
                Text(text = question.answer, style = typography.regular12, color = colors.deepMoss)
            }
            if (reportCount > 0) {
                ReleaseTag(
                    modifier = Modifier.padding(top = 2.dp),
                    text = pluralStringResource(id = R.plurals.report_count_tag, count = reportCount, reportCount),
                    containerColor = colors.softBlush,
                    contentColor = colors.brickRed
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = colors.taupe,
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ChapterQuestionCard(
            modifier = Modifier.fillMaxWidth(),
            number = 3,
            question = Question.dummy(),
            reportCount = 14,
            onClick = {}
        )
    }
}

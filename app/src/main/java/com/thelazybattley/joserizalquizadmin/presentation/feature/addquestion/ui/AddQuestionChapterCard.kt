package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Chapter
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

// Which chapter the question goes into, and how many it already has.
@Composable
fun AddQuestionChapterCard(
    modifier: Modifier = Modifier,
    bookTitle: String,
    chapter: Chapter
) {
    val shape = RoundedCornerShape(size = 14.dp)
    Row(
        modifier = modifier
            .background(color = colors.ivoryMist, shape = shape)
            .border(width = 1.dp, color = APP_BORDER_COLOR, shape = shape)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size = 32.dp)
                .background(color = colors.antiqueCream, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = chapter.chapterNumber.toString(),
                style = typography.semiBold13.copy(fontFamily = FontFamily.Monospace),
                color = colors.antiqueGold
            )
        }
        Column(
            modifier = Modifier.weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(space = 2.dp)
        ) {
            Text(
                text = chapter.chapterName,
                style = typography.bold14,
                color = colors.espresso,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(
                    id = R.string.chapter_card_detail,
                    bookTitle,
                    chapter.chapterNumber,
                    pluralStringResource(
                        id = R.plurals.questions_so_far,
                        count = chapter.questions.size,
                        chapter.questions.size
                    )
                ),
                style = typography.regular12,
                color = colors.woodsmokeBrown,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddQuestionChapterCard(
            modifier = Modifier.fillMaxWidth(),
            bookTitle = "Noli Me Tangere",
            chapter = Chapter.dummy(chapterNumber = 4)
        )
    }
}

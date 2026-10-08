package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Chapter
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.getTotalQuestions
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseTag
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@Composable
fun ContentItemCard(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    isExpanded: Boolean,
    callback: ContentCallback
) {
    val shape = RoundedCornerShape(size = 16.dp)
    Column(
        modifier = modifier
            .clip(shape = shape)
            .background(color = colors.ivoryMist, shape = shape)
            .border(width = 1.dp, color = APP_BORDER_COLOR, shape = shape)
    ) {
        BookSummary(quiz = quiz, isExpanded = isExpanded) {
            callback.handleAction(action = ContentActions.ExpandBook(id = if (isExpanded) null else quiz.id))
        }
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            val density = LocalDensity.current
            // Height of one chapter row plus its divider, measured from the first row.
            // Starts from an estimate so the scrolling list is never measured without a height limit.
            var chapterRowHeight by remember { mutableStateOf(value = ESTIMATED_CHAPTER_ROW_HEIGHT) }
            val isScrollable = quiz.chapters.size > VISIBLE_CHAPTER_ROWS
            Column(modifier = Modifier.background(color = colors.warmLinen.copy(alpha = 0.6f))) {
                HorizontalDivider(thickness = 1.dp, color = APP_BORDER_COLOR)
                // Capped at three and a half rows so the cut-off row shows the list scrolls.
                Column(
                    modifier = if (isScrollable) {
                        Modifier
                            .heightIn(max = chapterRowHeight * VISIBLE_CHAPTER_ROWS)
                            .verticalScroll(state = rememberScrollState())
                    } else {
                        Modifier
                    }
                ) {
                    quiz.chapters.forEachIndexed { index, chapter ->
                        if (index > 0) {
                            HorizontalDivider(thickness = 1.dp, color = APP_BORDER_COLOR.copy(alpha = 0.5f))
                        }
                        ChapterRow(
                            modifier = if (index == 0) {
                                Modifier.onSizeChanged { size ->
                                    chapterRowHeight = with(density) { size.height.toDp() } + 1.dp
                                }
                            } else {
                                Modifier
                            },
                            chapter = chapter
                        ) {
                            callback.handleAction(
                                action = ContentActions.Navigate(
                                    destination = ContentDestinations.AddQuestion(
                                        chapterNumber = chapter.chapterNumber,
                                        quizId = quiz.id
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private const val VISIBLE_CHAPTER_ROWS = 3.5f
private val ESTIMATED_CHAPTER_ROW_HEIGHT = 69.dp

@Composable
private fun BookSummary(
    quiz: Quiz,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "chevron_rotation"
    )
    val emptyChapters = quiz.chapters.count { it.questions.isEmpty() }
    val stateText = stringResource(id = if (isExpanded) R.string.expanded else R.string.collapsed)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { stateDescription = stateText }
            .padding(all = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Column(
            modifier = Modifier.weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(space = 2.dp)) {
                Text(
                    text = quiz.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.semiBold17,
                    color = colors.espresso
                )
                Text(
                    text = quiz.author,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.regular12,
                    color = colors.woodsmokeBrown
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReleaseTag(
                    text = stringResource(id = quiz.category.id).uppercase(),
                    containerColor = colors.parchment,
                    contentColor = colors.woodsmokeBrown
                )
                Text(
                    text = stringResource(
                        id = R.string.dot_separated,
                        pluralStringResource(id = R.plurals.chapter_count, count = quiz.chapters.size, quiz.chapters.size),
                        pluralStringResource(
                            id = R.plurals.question_count,
                            count = quiz.chapters.getTotalQuestions(),
                            quiz.chapters.getTotalQuestions()
                        )
                    ),
                    style = typography.regular12,
                    color = colors.woodsmokeBrown
                )
            }
            if (emptyChapters > 0) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(size = 6.dp)
                            .background(color = colors.antiqueGold, shape = CircleShape)
                    )
                    Text(
                        text = pluralStringResource(id = R.plurals.chapters_without_questions, count = emptyChapters, emptyChapters),
                        style = typography.semiBold12,
                        color = colors.antiqueGold
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .size(size = 32.dp)
                .background(
                    color = if (isExpanded) colors.warmLinen else colors.ivoryMist,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.rotate(degrees = chevronRotation),
                tint = colors.woodsmokeBrown
            )
        }
    }
}

@Composable
private fun ChapterRow(
    modifier: Modifier = Modifier,
    chapter: Chapter,
    onAddQuestion: () -> Unit
) {
    val addDescription = stringResource(id = R.string.add_question_to_chapter, chapter.chapterNumber, chapter.chapterName)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size = 28.dp)
                .background(color = colors.antiqueCream, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = chapter.chapterNumber.toString(),
                style = typography.semiBold12.copy(fontFamily = FontFamily.Monospace),
                color = colors.antiqueGold
            )
        }
        Column(
            modifier = Modifier.weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(space = 2.dp)
        ) {
            Text(
                text = chapter.chapterName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = typography.semiBold14,
                color = colors.espresso
            )
            if (chapter.questions.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_questions_yet),
                    style = typography.semiBold12,
                    color = colors.antiqueGold
                )
            } else {
                Text(
                    text = pluralStringResource(
                        id = R.plurals.question_count,
                        count = chapter.questions.size,
                        chapter.questions.size
                    ),
                    style = typography.regular12,
                    color = colors.woodsmokeBrown
                )
            }
        }
        OutlinedButton(
            modifier = Modifier
                .heightIn(min = 40.dp)
                .semantics { contentDescription = addDescription },
            onClick = onAddQuestion,
            shape = RoundedCornerShape(size = 10.dp),
            border = BorderStroke(width = 1.dp, color = colors.maroon.copy(alpha = 0.35f)),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
                tint = colors.maroon,
                modifier = Modifier.size(size = 16.dp)
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = stringResource(id = R.string.add_question_button),
                style = typography.bold12.copy(fontSize = typography.semiBold13.fontSize),
                color = colors.maroon
            )
        }
    }
}

class ContentExpandedStateProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(false, true)

    override fun getDisplayName(index: Int): String {
        return if (index == 0) "Collapsed" else "Expanded"
    }
}

@PreviewLightDark
@Composable
private fun ContentItemCardPreview(
    @PreviewParameter(ContentExpandedStateProvider::class) isExpanded: Boolean
) {
    AppTheme {
        ContentItemCard(
            modifier = Modifier.fillMaxWidth(),
            quiz = Quiz.dummy().copy(
                chapters = listOf(
                    Chapter.dummy(chapterNumber = 1),
                    Chapter.dummy(chapterNumber = 2),
                    Chapter.dummy(chapterNumber = 3).copy(questions = emptyList()),
                    Chapter.dummy(chapterNumber = 4),
                    Chapter.dummy(chapterNumber = 5),
                )
            ),
            isExpanded = isExpanded,
            callback = ContentCallback.default()
        )
    }
}

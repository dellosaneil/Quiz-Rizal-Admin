package com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.release.ReleaseChange
import com.thelazybattley.joserizalquizadmin.domain.model.release.ReleaseChangeKind
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateListRow
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun ReleaseChangeCard(
    modifier: Modifier = Modifier,
    change: ReleaseChange,
    // Null hides the Revert button, for changes that can't be reverted or while a push runs.
    onRevert: (() -> Unit)? = null
) {
    ModerateListRow(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(space = 12.dp)) {
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = change.title(),
                    style = typography.semiBold14,
                    color = colors.espresso,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.padding(top = 3.dp),
                    text = change.detail(),
                    style = typography.regular12,
                    color = colors.woodsmokeBrown
                )
            }
            ReleaseChangeKindTag(kind = change.kind)
        }
        if (onRevert != null) {
            TextButton(
                modifier = Modifier.padding(top = 4.dp),
                onClick = onRevert,
                contentPadding = PaddingValues(horizontal = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Undo,
                    contentDescription = null,
                    tint = colors.maroon,
                    modifier = Modifier.size(size = 16.dp)
                )
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = stringResource(id = R.string.revert),
                    style = typography.bold12.copy(fontSize = 13.sp),
                    color = colors.maroon
                )
            }
        }
    }
}

@Composable
fun ReleaseChangeKindTag(kind: ReleaseChangeKind) {
    when (kind) {
        ReleaseChangeKind.NEW -> ReleaseTag(
            text = stringResource(id = R.string.change_new),
            containerColor = colors.antiqueCream,
            contentColor = colors.antiqueGold
        )

        ReleaseChangeKind.EDITED -> ReleaseTag(
            text = stringResource(id = R.string.change_edited),
            containerColor = colors.parchment,
            contentColor = colors.woodsmokeBrown
        )

        ReleaseChangeKind.REMOVED -> ReleaseTag(
            text = stringResource(id = R.string.change_removed),
            containerColor = colors.softBlush,
            contentColor = colors.brickRed
        )
    }
}

@Composable
private fun ReleaseChange.title() = when (this) {
    is ReleaseChange.BookAdded -> bookTitle
    is ReleaseChange.BookDetailsEdited -> bookTitle
    is ReleaseChange.BookRemoved -> bookTitle
    is ReleaseChange.ChapterAdded -> stringResource(id = R.string.chapter_title, chapterNumber, chapterName)
    is ReleaseChange.ChapterRenamed -> stringResource(id = R.string.chapter_title, chapterNumber, chapterName)
    is ReleaseChange.ChapterRemoved -> stringResource(id = R.string.chapter_title, chapterNumber, chapterName)
    is ReleaseChange.QuestionAdded -> stringResource(id = R.string.quoted_value, question)
    is ReleaseChange.QuestionEdited -> stringResource(id = R.string.quoted_value, question)
    is ReleaseChange.QuestionRemoved -> stringResource(id = R.string.quoted_value, question)
}

@Composable
private fun ReleaseChange.detail() = when (this) {
    is ReleaseChange.BookAdded -> pluralStringResource(id = R.plurals.new_book_detail, count = chapterCount, chapterCount)
    is ReleaseChange.BookDetailsEdited -> stringResource(id = R.string.book_details_changed)
    is ReleaseChange.BookRemoved -> stringResource(id = R.string.book_removed_from_debug)
    is ReleaseChange.ChapterAdded -> pluralStringResource(
        id = R.plurals.new_chapter_detail,
        count = questionCount,
        bookTitle,
        questionCount
    )

    is ReleaseChange.ChapterRenamed -> stringResource(id = R.string.chapter_renamed_in, bookTitle)
    is ReleaseChange.ChapterRemoved -> stringResource(id = R.string.removed_from, bookTitle)
    is ReleaseChange.QuestionAdded -> stringResource(id = R.string.book_chapter, bookTitle, chapterNumber)
    is ReleaseChange.QuestionEdited -> stringResource(
        id = when {
            choicesChanged && answerChanged -> R.string.question_choices_and_answer_changed
            choicesChanged -> R.string.question_choices_changed
            else -> R.string.question_answer_changed
        },
        bookTitle,
        chapterNumber
    )

    is ReleaseChange.QuestionRemoved -> stringResource(id = R.string.book_chapter, bookTitle, chapterNumber)
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ReleaseChangeCard(
            modifier = Modifier.fillMaxWidth(),
            change = ReleaseChange.ChapterAdded(
                bookTitle = "El Filibusterismo",
                chapterNumber = 1,
                chapterName = "Simoun Returns",
                questionCount = 4
            )
        )
    }
}

package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookState
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateListRow
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseTag
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonButton
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@Composable
fun AddBookPublishedContent(
    modifier: Modifier = Modifier,
    state: AddBookState,
    quizId: String,
    callback: AddBookCallback
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .verticalScroll(state = rememberScrollState())
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(space = 22.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(size = 56.dp)
                    .background(color = colors.deepMoss, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = colors.softSage,
                    modifier = Modifier.size(size = 28.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                Text(
                    text = stringResource(id = R.string.book_in_debug, state.title.trim()),
                    style = typography.bold23.copy(fontSize = 28.sp, lineHeight = 34.sp),
                    color = colors.espresso
                )
                Text(
                    text = stringResource(id = R.string.book_in_debug_body),
                    style = typography.regular13.copy(fontSize = 14.sp, lineHeight = 22.sp),
                    color = colors.woodsmokeBrown
                )
            }
            BookSummaryCard(modifier = Modifier.fillMaxWidth(), state = state)
        }
        Column(
            modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(space = 10.dp)
        ) {
            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 52.dp),
                text = stringResource(id = R.string.add_questions)
            ) {
                callback.handleAction(
                    action = AddBookActions.NavigateDestination(destination = AddBookDestinations.AddQuestion(quizId = quizId))
                )
            }
            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 52.dp),
                text = stringResource(id = R.string.add_another_book),
                buttonColors = ButtonDefaults.buttonColors(
                    containerColor = colors.warmLinen,
                    contentColor = colors.espresso
                ),
                border = BorderStroke(width = 1.dp, color = APP_BORDER_COLOR)
            ) {
                callback.handleAction(
                    action = AddBookActions.NavigateDestination(destination = AddBookDestinations.AddAnotherBook)
                )
            }
        }
    }
}

@Composable
private fun BookSummaryCard(modifier: Modifier = Modifier, state: AddBookState) {
    ModerateListRow(modifier = modifier) {
        Row(verticalAlignment = Alignment.Top) {
            Column(
                modifier = Modifier.weight(weight = 1f),
                verticalArrangement = Arrangement.spacedBy(space = 2.dp)
            ) {
                Text(text = state.title.trim(), style = typography.semiBold17, color = colors.espresso)
                Text(text = state.author.trim(), style = typography.regular12, color = colors.woodsmokeBrown)
            }
            ReleaseTag(
                text = stringResource(id = state.category.id).uppercase(),
                containerColor = colors.parchment,
                contentColor = colors.woodsmokeBrown
            )
        }
        Spacer(modifier = Modifier.height(height = 12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
            SummaryCount(
                modifier = Modifier.weight(weight = 1f),
                count = state.chapters.size,
                label = pluralStringResource(id = R.plurals.chapters_label, count = state.chapters.size)
            )
            SummaryCount(
                modifier = Modifier.weight(weight = 1f),
                count = 0,
                label = pluralStringResource(id = R.plurals.questions_label, count = 0)
            )
        }
    }
}

@Composable
private fun SummaryCount(modifier: Modifier = Modifier, count: Int, label: String) {
    Column(
        modifier = modifier
            .background(color = colors.warmLinen, shape = RoundedCornerShape(size = 12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(text = count.toString(), style = typography.bold23.copy(fontSize = 20.sp), color = colors.espresso)
        Text(text = label, style = typography.regular11, color = colors.woodsmokeBrown)
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddBookPublishedContent(
            modifier = Modifier
                .background(color = colors.warmLinen)
                .fillMaxSize(),
            state = AddBookState(
                title = "Noli Me Tangere",
                author = "José Rizal",
                chapters = listOf("Isang Pagtitipon", "Crisostomo Ibarra", "Ang Hapunan")
            ),
            quizId = "",
            callback = AddBookCallback.default()
        )
    }
}

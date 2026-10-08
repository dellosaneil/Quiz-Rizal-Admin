package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateDestinations
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun ModerateSuggestedBookCard(
    modifier: Modifier = Modifier,
    suggestedBook: RankedSuggestedBook,
    callbacks: ModerateCallback
) {
    ModerateListRow(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(space = 10.dp)) {
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = suggestedBook.bookTitle,
                    style = typography.semiBold14,
                    color = colors.espresso
                )
                if (suggestedBook.author.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 3.dp),
                        text = suggestedBook.author,
                        style = typography.regular11,
                        color = colors.woodsmokeBrown
                    )
                }
            }
            ModerateFrequencyBadge(count = suggestedBook.requestCount, labelRes = R.plurals.students)
        }
        Text(
            text = stringResource(id = R.string.plus_add_book),
            style = typography.bold12,
            color = colors.white,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 11.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(size = 9.dp))
                .background(color = colors.maroon)
                .clickable {
                    callbacks.handleAction(
                        action = ModerateActions.NavigateDestination(
                            destination = ModerateDestinations.AddBook(
                                bookTitle = suggestedBook.bookTitle,
                                author = suggestedBook.author
                            )
                        )
                    )
                }
                .padding(all = 8.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ModerateSuggestedBookCard(
            modifier = Modifier.fillMaxWidth(),
            suggestedBook = RankedSuggestedBook.dummy(),
            callbacks = ModerateCallback.default()
        )
    }
}

package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
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
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@Composable
fun ModerateSuggestedBookCard(
    modifier: Modifier = Modifier,
    rank: Int,
    suggestedBook: RankedSuggestedBook,
    callbacks: ModerateCallback
) {
    ModerateListRow(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(space = 12.dp)) {
            Text(
                modifier = Modifier
                    .padding(top = 3.dp)
                    .width(width = 20.dp),
                text = stringResource(id = R.string.rank_value, rank),
                style = typography.semiBold12.copy(fontFamily = FontFamily.Monospace),
                color = colors.taupe
            )
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = suggestedBook.bookTitle,
                    style = typography.semiBold16,
                    color = colors.espresso
                )
                if (suggestedBook.author.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 3.dp),
                        text = suggestedBook.author,
                        style = typography.regular12,
                        color = colors.woodsmokeBrown
                    )
                }
            }
            ModerateFrequencyBadge(count = suggestedBook.requestCount, labelRes = R.plurals.students)
        }
        Row(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.height(height = 44.dp),
                onClick = {
                    callbacks.handleAction(
                        action = ModerateActions.RequestRemoveSuggestion(suggestedBook = suggestedBook)
                    )
                },
                shape = RoundedCornerShape(size = 10.dp),
                border = BorderStroke(width = 1.dp, color = APP_BORDER_COLOR),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.brickRed),
                contentPadding = PaddingValues(horizontal = 14.dp)
            ) {
                ModerateCardButtonContent(
                    icon = { Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, modifier = it) },
                    text = stringResource(id = R.string.remove)
                )
            }
            Button(
                modifier = Modifier
                    .weight(weight = 1f)
                    .height(height = 44.dp),
                onClick = {
                    callbacks.handleAction(
                        action = ModerateActions.NavigateDestination(
                            destination = ModerateDestinations.AddBook(
                                bookTitle = suggestedBook.bookTitle,
                                author = suggestedBook.author
                            )
                        )
                    )
                },
                shape = RoundedCornerShape(size = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.maroon,
                    contentColor = colors.white
                )
            ) {
                ModerateCardButtonContent(
                    icon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = null, modifier = it) },
                    text = stringResource(id = R.string.add_book)
                )
            }
        }
    }
}

@Composable
private fun ModerateCardButtonContent(
    icon: @Composable (Modifier) -> Unit,
    text: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon(Modifier.size(size = 16.dp))
        Text(text = text, style = typography.bold12)
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ModerateSuggestedBookCard(
            modifier = Modifier.fillMaxWidth(),
            rank = 1,
            suggestedBook = RankedSuggestedBook.dummy(),
            callbacks = ModerateCallback.default()
        )
    }
}

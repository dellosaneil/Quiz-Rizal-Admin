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
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateDestinations
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

// Rank column width plus the gap after it, so the lines below line up with the title.
private val CONTENT_INDENT = 34.dp

@Composable
fun ModerateSuggestedBookCard(
    modifier: Modifier = Modifier,
    rank: Int,
    suggestedBook: RankedSuggestedBook,
    isInLibrary: Boolean,
    callbacks: ModerateCallback
) {
    ModerateListRow(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(space = 12.dp)) {
            Text(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .width(width = 22.dp),
                text = rank.toString(),
                style = typography.semiBold13.copy(fontFamily = FontFamily.Monospace),
                color = colors.antiqueGold
            )
            Column(
                modifier = Modifier.weight(weight = 1f),
                verticalArrangement = Arrangement.spacedBy(space = 2.dp)
            ) {
                Text(
                    text = suggestedBook.bookTitle,
                    style = typography.semiBold17,
                    color = colors.espresso
                )
                if (suggestedBook.author.isNotEmpty()) {
                    Text(
                        text = suggestedBook.author,
                        style = typography.regular12,
                        color = colors.woodsmokeBrown
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = suggestedBook.requestCount.toString(),
                    style = typography.bold23.copy(fontSize = 20.sp, lineHeight = 22.sp),
                    color = colors.espresso
                )
                Text(
                    text = pluralStringResource(id = R.plurals.students_label, count = suggestedBook.requestCount),
                    style = typography.regular11,
                    color = colors.woodsmokeBrown
                )
            }
        }
        if (isInLibrary) {
            Row(
                modifier = Modifier.padding(start = CONTENT_INDENT, top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = colors.deepMoss,
                    modifier = Modifier.size(size = 14.dp)
                )
                Text(
                    text = stringResource(id = R.string.already_in_library),
                    style = typography.semiBold12,
                    color = colors.deepMoss
                )
            }
        }
        Row(
            modifier = Modifier.padding(start = CONTENT_INDENT, top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.height(height = 40.dp),
                onClick = {
                    callbacks.handleAction(
                        action = ModerateActions.RequestRemoveSuggestion(suggestedBook = suggestedBook)
                    )
                },
                shape = RoundedCornerShape(size = 10.dp),
                border = BorderStroke(width = 1.dp, color = APP_BORDER_COLOR),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.espresso),
                contentPadding = PaddingValues(horizontal = 14.dp)
            ) {
                ModerateCardButtonContent(
                    icon = Icons.Outlined.Delete,
                    text = stringResource(id = if (isInLibrary) R.string.clear else R.string.remove)
                )
            }
            if (!isInLibrary) {
                Button(
                    modifier = Modifier.height(height = 40.dp),
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
                    ),
                    contentPadding = PaddingValues(start = 10.dp, end = 14.dp)
                ) {
                    ModerateCardButtonContent(
                        icon = Icons.Rounded.Add,
                        text = stringResource(id = R.string.add_book_short)
                    )
                }
            }
        }
    }
}

@Composable
fun ModerateCardButtonContent(
    icon: ImageVector?,
    text: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(size = 16.dp))
        }
        Text(text = text, style = typography.bold12.copy(fontSize = 13.sp))
    }
}

private class InLibraryProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(false, true)
}

@PreviewLightDark
@Composable
private fun Preview(@PreviewParameter(InLibraryProvider::class) isInLibrary: Boolean) {
    AppTheme {
        ModerateSuggestedBookCard(
            modifier = Modifier.fillMaxWidth(),
            rank = 1,
            suggestedBook = RankedSuggestedBook.dummy(),
            isInLibrary = isInLibrary,
            callbacks = ModerateCallback.default()
        )
    }
}

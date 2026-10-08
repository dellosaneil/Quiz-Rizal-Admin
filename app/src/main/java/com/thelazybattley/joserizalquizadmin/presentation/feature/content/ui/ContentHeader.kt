package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun ContentHeader(
    modifier: Modifier = Modifier,
    bookCount: Int?,
    questionCount: Int,
    onAddBook: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Column(
            modifier = Modifier.weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(space = 2.dp)
        ) {
            Text(
                modifier = Modifier.semantics { heading() },
                text = stringResource(id = R.string.library),
                style = typography.bold16.copy(fontSize = 18.sp),
                color = colors.espresso
            )
            // Null while the library is still loading.
            if (bookCount != null) {
                Text(
                    text = if (bookCount == 0) {
                        stringResource(id = R.string.library_empty_summary)
                    } else {
                        stringResource(
                            id = R.string.library_summary,
                            pluralStringResource(id = R.plurals.book_count, count = bookCount, bookCount),
                            pluralStringResource(id = R.plurals.question_count, count = questionCount, questionCount)
                        )
                    },
                    style = typography.regular12,
                    color = colors.woodsmokeBrown
                )
            }
        }
        if (bookCount != 0) {
            ContentAddBookButton(onClick = onAddBook)
        }
    }
}

@Composable
fun ContentAddBookButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.add_book_short),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(height = 40.dp),
        onClick = onClick,
        shape = RoundedCornerShape(size = 10.dp),
        contentPadding = PaddingValues(start = 10.dp, end = 14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.maroon,
            contentColor = colors.white
        )
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            modifier = Modifier.size(size = 18.dp)
        )
        Text(
            modifier = Modifier.padding(start = 6.dp),
            text = text,
            style = typography.bold12.copy(fontSize = 13.sp)
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ContentHeader(
            modifier = Modifier.fillMaxWidth(),
            bookCount = 3,
            questionCount = 40,
            onAddBook = {}
        )
    }
}

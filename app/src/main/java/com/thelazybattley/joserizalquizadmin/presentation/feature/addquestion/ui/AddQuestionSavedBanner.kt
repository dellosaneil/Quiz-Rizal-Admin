package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.SavedQuestion
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun AddQuestionSavedBanner(
    modifier: Modifier = Modifier,
    saved: SavedQuestion,
    chapterNumber: Int
) {
    Row(
        modifier = modifier
            .background(color = colors.softSage, shape = RoundedCornerShape(size = 14.dp))
            .semantics { liveRegion = LiveRegionMode.Polite }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size = 28.dp)
                .background(color = colors.deepMoss, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = colors.softSage,
                modifier = Modifier.size(size = 16.dp)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(space = 2.dp)) {
            Text(
                text = stringResource(id = R.string.question_saved_to, saved.number, chapterNumber),
                style = typography.bold12.copy(fontSize = 13.sp),
                color = colors.deepMoss
            )
            Text(
                text = stringResource(id = R.string.quoted, saved.question),
                style = typography.regular12,
                color = colors.deepMoss,
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
        AddQuestionSavedBanner(
            modifier = Modifier.fillMaxWidth(),
            saved = SavedQuestion(number = 7, question = "Who tells Ibarra what happened to his father, Don Rafael?"),
            chapterNumber = 4
        )
    }
}

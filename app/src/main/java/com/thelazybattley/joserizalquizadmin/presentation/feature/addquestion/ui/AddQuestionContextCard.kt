package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_PADDING

@Composable
fun AddQuestionContextCard(
    modifier: Modifier = Modifier,
    quizName: String,
    chapterNumber: Int
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colors.parchment
        ),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(
            modifier = Modifier.padding(all = APP_PADDING),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.adding_to),
                style = typography.semiBold10,
                color = colors.taupe
            )
            val chapterLabel = stringResource(id = R.string.chapter_value, chapterNumber)
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                Text(text = quizName, style = typography.bold14, color = colors.espresso)
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = null,
                    tint = colors.taupe,
                    modifier = Modifier.size(size = 14.dp)
                )
                Text(
                    text = chapterLabel,
                    style = typography.bold14,
                    color = colors.espresso
                )
            }

            Text(
                text = stringResource(id = R.string.you_are_adding_a_question_to_this_chapter),
                style = typography.regular11,
                color = colors.taupe
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddQuestionContextCard(
            modifier = Modifier.fillMaxWidth(),
            quizName = "Jose Rizal",
            chapterNumber = 1
        )
    }
}

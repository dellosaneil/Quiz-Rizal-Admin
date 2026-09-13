package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun AddQuestionSuccessBanner(modifier: Modifier = Modifier, chapterNumber: Int) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colors.softSage
        ),
        border = BorderStroke(width = 1.dp, color = colors.deepMoss)
    ) {
        Row(
            modifier = Modifier.padding(all = APP_PADDING),
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
        ) {
            Icon(
                modifier = Modifier.padding(top = 2.dp),
                painter = painterResource(id = R.drawable.ic_check),
                tint = colors.deepMoss,
                contentDescription = null,
            )
            Column(
                modifier = Modifier.weight(weight = 1f),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.question_saved_to, chapterNumber),
                    style = typography.bold12,
                    color = colors.espresso
                )
                Text(
                    text = stringResource(id = R.string.add_another_question_below),
                    style = typography.regular11,
                    color = colors.woodsmokeBrown
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddQuestionSuccessBanner(modifier = Modifier.fillMaxWidth(), chapterNumber = 1)
    }
}

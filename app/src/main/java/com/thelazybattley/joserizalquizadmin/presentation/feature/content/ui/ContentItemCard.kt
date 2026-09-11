package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.getTotalQuestions
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_PADDING

@Composable
fun ContentItemCard(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    isExpanded: Boolean,
    callback: ContentCallback
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colors.ivoryMist
        ),
        elevation = CardDefaults.elevatedCardElevation(),
        onClick = {
            if (isExpanded) {
                callback.handleAction(action = ContentActions.ExpandBook(id = null))
                return@Card
            }
            callback.handleAction(action = ContentActions.ExpandBook(id = quiz.id))
        }
    ) {
        Row(
            modifier = Modifier.padding(all = APP_PADDING)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                modifier = Modifier.weight(weight = 1f)
            ) {
                Text(
                    text = quiz.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.bold16,
                    color = colors.espresso,
                )
                Text(
                    text = quiz.subtitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.regular12,
                    color = colors.woodsmokeBrown,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = quiz.category.id),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.regular12,
                        color = colors.woodsmokeBrown,
                        modifier = Modifier
                            .background(
                                color = colors.parchment,
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .padding(all = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.value_chapters, quiz.chapters.size),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.regular12,
                        color = colors.woodsmokeBrown,
                        modifier = Modifier
                            .background(
                                color = colors.parchment,
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .padding(all = 8.dp)
                    )

                    Text(
                        text = stringResource(
                            id = R.string.value_questions,
                            quiz.chapters.getTotalQuestions()
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.regular12,
                        color = colors.woodsmokeBrown,
                        modifier = Modifier
                            .background(
                                color = colors.parchment,
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .padding(all = 8.dp)
                    )
                }
            }
            val degrees = if (isExpanded) {
                270f
            } else {
                90f
            }


            Icon(
                painter = painterResource(id = R.drawable.ic_chevron),
                contentDescription = null,
                modifier = Modifier
                    .rotate(degrees = degrees)
                    .size(size = 16.dp)
                    .align(alignment = Alignment.CenterVertically),
                tint = colors.taupe
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ContentItemCard(
            modifier = Modifier.fillMaxWidth(),
            quiz = Quiz.dummy(),
            isExpanded = false,
            callback = ContentCallback.default()
        )
    }
}

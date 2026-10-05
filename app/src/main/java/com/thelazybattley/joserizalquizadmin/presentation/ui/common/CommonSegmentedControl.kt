package com.thelazybattley.joserizalquizadmin.presentation.ui.common

import androidx.annotation.StringRes
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

@Composable
fun <T> CommonSegmentedControl(
    modifier: Modifier = Modifier,
    selectedOption: T,
    callback: (T) -> Unit,
    choices: List<T>,
    @StringRes stringResources: List<Int>,
    @StringRes titleRes: Int? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        titleRes?.let { title ->
            Text(
                text = stringResource(id = title),
                style = typography.semiBold10,
                color = colors.taupe
            )
        }
        Row(
            modifier = Modifier
                .background(color = colors.parchment, shape = RoundedCornerShape(size = 8.dp))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            choices.forEachIndexed { index, choice ->
                val config = if (choice == selectedOption) {
                    CommonSegmentedControlConfig(
                        textColor = colors.espresso,
                        backgroundColor = colors.ivoryMist
                    )
                } else {
                    CommonSegmentedControlConfig(
                        textColor = colors.woodsmokeBrown,
                        backgroundColor = colors.parchment
                    )
                }

                Text(
                    text = stringResource(id = stringResources[index]),
                    style = typography.bold12,
                    color = config.textColor,
                    modifier = Modifier
                        .padding(all = 6.dp)
                        .clickable {
                            callback(choice)
                        }
                        .clip(shape = RoundedCornerShape(size = 8.dp))
                        .background(
                            color = config.backgroundColor,
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                        .padding(all = 12.dp)
                        .weight(weight = 1f),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

private data class CommonSegmentedControlConfig(
    val textColor: Color,
    val backgroundColor: Color
)

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        CommonSegmentedControl(
            modifier = Modifier.fillMaxWidth(),
            selectedOption = Category.LIFE_OF_RIZAL,
            callback = {

            },
            choices = Category.entries,
            stringResources = Category.entries.map { it.id }
        )
    }
}

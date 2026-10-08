package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@Composable
fun AddBookChapterRow(
    modifier: Modifier = Modifier,
    index: Int,
    text: String,
    canRemove: Boolean,
    enabled: Boolean = true,
    callback: AddBookCallback
) {
    val shape = RoundedCornerShape(size = 14.dp)
    val chapterNumber = index.inc()
    val fieldDescription = stringResource(id = R.string.chapter_name_description, chapterNumber)
    Row(
        modifier = modifier
            .background(color = colors.ivoryMist, shape = shape)
            .border(width = 1.dp, color = APP_BORDER_COLOR, shape = shape)
            .padding(start = 12.dp, top = 4.dp, end = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size = 28.dp)
                .background(color = colors.antiqueCream, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = chapterNumber.toString(),
                style = typography.semiBold12.copy(fontFamily = FontFamily.Monospace),
                color = colors.antiqueGold
            )
        }
        BasicTextField(
            modifier = Modifier
                .weight(weight = 1f)
                .height(height = 44.dp)
                .semantics { contentDescription = fieldDescription },
            value = text,
            onValueChange = { newValue ->
                callback.handleAction(action = AddBookActions.Chapter.Update(index = index, text = newValue))
            },
            enabled = enabled,
            singleLine = true,
            textStyle = typography.regular13.copy(fontSize = 15.sp, color = colors.espresso),
            cursorBrush = SolidColor(value = colors.maroon),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.chapter_name_placeholder),
                            style = typography.regular13.copy(fontSize = 15.sp),
                            color = colors.taupe
                        )
                    }
                    innerTextField()
                }
            }
        )
        IconButton(
            onClick = { callback.handleAction(action = AddBookActions.Chapter.Delete(index = index)) },
            enabled = canRemove && enabled,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = colors.woodsmokeBrown,
                disabledContentColor = colors.taupe.copy(alpha = 0.4f)
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(id = R.string.remove_chapter, chapterNumber),
                modifier = Modifier.size(size = 18.dp)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddBookChapterRow(
            modifier = Modifier.fillMaxWidth(),
            index = 1,
            text = "Crisostomo Ibarra",
            canRemove = true,
            callback = AddBookCallback.default()
        )
    }
}

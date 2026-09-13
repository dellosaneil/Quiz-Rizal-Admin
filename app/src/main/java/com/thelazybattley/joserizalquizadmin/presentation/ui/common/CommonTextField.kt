package com.thelazybattley.joserizalquizadmin.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text
    ),
    onKeyboardAction: KeyboardActionHandler? = null,
    placeholderText: String? = null,
    textStyle: TextStyle = typography.semiBold18,
    textAlign: TextAlign = TextAlign.Center,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    contentAlignment: Alignment? = null
) {
    BasicTextField(
        modifier = modifier
            .padding(vertical = 12.dp)
            .border(
                width = 1.dp,
                color = colors.espresso,
                shape = RoundedCornerShape(size = 12.dp)
            )
            .background(color = colors.ivoryMist, shape = RoundedCornerShape(size = 12.dp))
            .clip(shape = RoundedCornerShape(size = 12.dp)),
        state = state,
        textStyle = textStyle.copy(
            textAlign = textAlign,
            color = colors.espresso
        ),
        lineLimits = lineLimits,
        cursorBrush = SolidColor(value = colors.maroon),
        decorator = TextFieldDecorator { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = contentAlignment ?: when (textAlign) {
                    TextAlign.Center -> Alignment.Center
                    TextAlign.Right, TextAlign.End -> Alignment.CenterEnd
                    else -> Alignment.CenterStart
                }
            ) {
                if (placeholderText != null && state.text.isEmpty()) {
                    Text(
                        text = placeholderText,
                        style = textStyle,
                        color = colors.taupe,
                        textAlign = textAlign,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                innerTextField()
            }
        },
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        CommonTextField(
            modifier = Modifier
                .height(height = 56.dp)
                .fillMaxWidth(),
            state = rememberTextFieldState()
        )
    }

}

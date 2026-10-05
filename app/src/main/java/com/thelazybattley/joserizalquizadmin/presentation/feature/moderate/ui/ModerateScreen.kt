package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateState

@Composable
fun ModerateScreen(
    modifier: Modifier = Modifier,
    state: ModerateState,
    callbacks: ModerateCallback
) {

}


@PreviewLightDark
@Composable
private fun Preview() {
    ModerateScreen(
        modifier = Modifier,
        state = ModerateState(),
        callbacks = ModerateCallback.default()
    )
}

package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateContentFeedback

data class ModerateState(
    val isLoading: Boolean = true,
    val selectedFeedback: ModerateContentFeedback = ModerateContentFeedback.SUGGESTED_BOOKS
) : BaseState

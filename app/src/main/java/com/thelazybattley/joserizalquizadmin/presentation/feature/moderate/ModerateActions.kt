package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

import com.thelazybattley.joserizalquizadmin.base.BaseActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateContentFeedback

sealed class ModerateActions: BaseActions {

    data class SelectFeedbackType(val feedbackType: ModerateContentFeedback): ModerateActions()

}

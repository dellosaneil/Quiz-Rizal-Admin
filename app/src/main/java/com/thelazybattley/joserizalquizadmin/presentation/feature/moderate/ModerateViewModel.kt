package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ModerateViewModel @Inject constructor() : BaseViewModel<ModerateState, ModerateActions>(
    initialState = ModerateState()
), ModerateCallback {
    override fun handleAction(action: ModerateActions) {
        when(action) {
            is ModerateActions.SelectFeedbackType -> updateState(newState = state.value.copy(selectedFeedback = action.feedbackType))
        }
    }
}

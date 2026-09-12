package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion

import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.GetQuizByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddQuestionViewModel @Inject constructor(
    private val getQuizByIdUseCase: GetQuizByIdUseCase
) :
    BaseViewModel<AddQuestionState, AddQuestionAction>(
        initialState = AddQuestionState()
    ), AddQuestionCallback {

    override fun handleAction(action: AddQuestionAction) {
        when (action) {
            is AddQuestionAction.Choice -> {
                when (action) {
                    is AddQuestionAction.Choice.Selected -> updateState(
                        newState = state.value.copy(
                            correctAnswerIndex = action.index
                        )
                    )
                    is AddQuestionAction.Choice.UpdateValue -> updateState(
                        newState = state.value.copy(
                            choices = state.value.choices.toMutableList().apply {
                                this[action.index] = action.choice
                            }
                        )
                    )
                }
            }
            is AddQuestionAction.Navigate -> updateState(
                newState = state.value.copy(
                    destination = action.destination
                )
            )

            is AddQuestionAction.UpdateQuestion -> TODO()
        }
    }
}

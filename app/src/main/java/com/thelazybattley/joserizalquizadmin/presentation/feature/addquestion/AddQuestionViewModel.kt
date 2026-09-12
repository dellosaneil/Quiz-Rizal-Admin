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
        TODO("Not yet implemented")
    }

}

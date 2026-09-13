package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion

import com.thelazybattley.joserizalquizadmin.base.BaseActions

sealed class AddQuestionAction : BaseActions {

    sealed class Choice : AddQuestionAction() {
        data class Selected(val index: Int) : Choice()
        data class UpdateValue(val index: Int, val choice: String) : Choice()
    }

    data class UpdateQuestion(val question: String) : AddQuestionAction()

    data class Navigate(val destination: AddQuestionDestinations?) : AddQuestionAction()

    object SaveQuestion : AddQuestionAction()

    object ResetBanner: AddQuestionAction()
}

package com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion

import com.thelazybattley.joserizalquizadmin.base.BaseActions

sealed class EditQuestionActions : BaseActions {

    data class UpdateQuestion(val question: String) : EditQuestionActions()

    data class UpdateChoice(val index: Int, val choice: String) : EditQuestionActions()

    data class SelectAnswer(val index: Int) : EditQuestionActions()

    data object Revert : EditQuestionActions()

    data object ToggleClearReports : EditQuestionActions()

    data object Save : EditQuestionActions()

    data object RequestDelete : EditQuestionActions()

    data object CancelDelete : EditQuestionActions()

    data object ConfirmDelete : EditQuestionActions()

    data class Navigate(val destination: EditQuestionDestinations?) : EditQuestionActions()
}
